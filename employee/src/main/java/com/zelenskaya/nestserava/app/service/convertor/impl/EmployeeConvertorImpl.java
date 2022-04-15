package com.zelenskaya.nestserava.app.service.convertor.impl;

import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.service.GetLegalByIdService;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmployeeConvertorImpl implements EmployeeConvertor {
    private final GetLegalByIdService getLegalByIdService;

    @Override
    @Transactional
    public AddEmployeeDTO convertToDTO(Employee employee) {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(employee.getId());
        addEmployeeDTO.setName(employee.getName());
        addEmployeeDTO.setRecruitmentDate(employee.getRecruitmentDate());
        addEmployeeDTO.setTerminationDate(employee.getTerminationDate());
        String nameLegal = generateNameLegal(employee);
        addEmployeeDTO.setNameLegal(nameLegal);
        addEmployeeDTO.setIbanByn(employee.getIbanByn());
        addEmployeeDTO.setIbanCurrency(employee.getIbanCurrency());
        return addEmployeeDTO;
    }

    private String generateNameLegal(Employee employee) {
        ResponseEntity<Object> responseEntity = getLegalByIdService.getLegalById(employee.getLegalId());
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                List entityBody = (List) responseEntity.getBody();
                assert entityBody != null;
                Map<String, Object> body = (Map<String, Object>) entityBody.get(0);
                return body.get("Name_Legal").toString();
            } catch (IndexOutOfBoundsException e) {
                throw new NoResultException("Компания не найдена");
            }
        } else {
            throw new NoResultException("Компания не найдена");
        }
    }
}