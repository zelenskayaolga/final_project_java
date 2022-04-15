package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeDetailsRepository;
import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.PositionLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.EmployeeDetails;
import com.zelenskaya.nestserava.app.repository.model.PositionEnum;
import com.zelenskaya.nestserava.app.repository.model.PositionLegal;
import com.zelenskaya.nestserava.app.service.AddEmployeeService;
import com.zelenskaya.nestserava.app.service.GetLegalService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.config.ServiceConfig;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.exception.ServiceExceptionConflict;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.AddedEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.PositionEnumDTO;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddEmployeeServiceImpl implements AddEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final LocalDateService localDateService;
    private final ServiceConfig serviceConfig;
    private final GetLegalService getService;
    private final PositionLegalRepository positionLegalRepository;

    @Override
    @Transactional
    public AddedEmployeeDTO add(AddEmployeeDTO addEmployeeDTO) {
        Employee employee = convertToEmployee(addEmployeeDTO);
        EmployeeDetails employeeDetails = generateDetails(serviceConfig.getZoneTime());
        employee.setEmployeeDetails(employeeDetails);
        employeeDetails.setEmployee(employee);
        try {
            employeeDetailsRepository.add(employeeDetails);
            employeeRepository.add(employee);
            return convertToDTO(employee);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceExceptionConflict("Сотрудник существует");
        }
    }

    private AddedEmployeeDTO convertToDTO(Employee employee) {
        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(employee.getId());
        return addedEmployeeDTO;
    }

    private Employee convertToEmployee(AddEmployeeDTO addEmployeeDTO) throws ServiceEmployeeException {
        Employee employee = new Employee();
        employee.setId(addEmployeeDTO.getId());
        employee.setName(addEmployeeDTO.getName());
        LocalDate recruitmentDate = generateRecruitmentDate(addEmployeeDTO);
        employee.setRecruitmentDate(recruitmentDate);
        LocalDate terminationDate = generateTerminationDate(addEmployeeDTO);
        employee.setTerminationDate(terminationDate);
        Long legalId = generateLegalId(addEmployeeDTO.getNameLegal());
        employee.setLegalId(legalId);
        String ibanByn = addEmployeeDTO.getIbanByn();
        employee.setIbanByn(ibanByn);
        String ibanCurrency = addEmployeeDTO.getIbanCurrency();
        employee.setIbanCurrency(ibanCurrency);
        PositionLegal positionLegal = generatePositionLegal();
        employee.setPosition(positionLegal);
        return employee;
    }

    private PositionLegal generatePositionLegal() {
        String position = PositionEnumDTO.HIRED.name();
        Optional<PositionLegal> positionLegal = positionLegalRepository.findByPosition(PositionEnum.valueOf(position));
        return positionLegal.orElse(null);
    }

    private Long generateLegalId(String nameLegal) {
        try {
            ResponseEntity<Object> responseEntity = getService.getLegalByName(nameLegal);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
                assert body != null;
                return Long.parseLong(body.get("LegalId").toString());
            } else {
                throw new NoResultException("Компания по заданным параметрам не найдена");
            }
        } catch (FeignException e) {
            throw new NoResultException("Компания по заданным параметрам не найдена");
        }
    }

    private LocalDate generateTerminationDate(AddEmployeeDTO addEmployeeDTO) throws ServiceEmployeeException {
        LocalDate recruitmentDate = addEmployeeDTO.getRecruitmentDate();
        LocalDate terminationDate = addEmployeeDTO.getTerminationDate();
        if (recruitmentDate.isBefore(terminationDate)) {
            return terminationDate;
        } else {
            throw new ServiceEmployeeException("Введите правильную дату");
        }
    }

    private LocalDate generateRecruitmentDate(AddEmployeeDTO addEmployeeDTO) throws ServiceEmployeeException {
        LocalDate recruitmentDate = addEmployeeDTO.getRecruitmentDate();
        if (recruitmentDate.equals(LocalDate.now())) {
            return recruitmentDate;
        } else {
            throw new ServiceEmployeeException("Введите правильную дату");
        }
    }

    private EmployeeDetails generateDetails(String timeZone) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        LocalDate createDate = generateCreateDate(timeZone);
        employeeDetails.setCreateDate(createDate);
        employeeDetails.setUpdateDate(createDate);
        return employeeDetails;
    }

    private LocalDate generateCreateDate(String timeZone) {
        return localDateService.dateTimeWithZone(timeZone);
    }
}
