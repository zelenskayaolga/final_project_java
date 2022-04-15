package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.SearchEmployee;
import com.zelenskaya.nestserava.app.service.GetLegalByNameAndUnpService;
import com.zelenskaya.nestserava.app.service.SearchEmployeeService;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchEmployeeServiceImpl implements SearchEmployeeService {
    private final ServiceEmployeeConstants constants;
    private final EmployeeRepository employeeRepository;
    private final EmployeeConvertor employeeConvertor;
    private final GetLegalByNameAndUnpService getService;

    @Override
    public List<AddEmployeeDTO> get(SearchDTO searchDTO, PaginationDTO paginationDTO) throws ServiceEmployeeException {
        Page<Employee> employees;
        SearchEmployee searchEmployee = convert(searchDTO);
        try {
            switch (paginationDTO.getPagination()) {
                case DEFAULT: {
                    employees = getEmployees(searchEmployee, paginationDTO.getPage(), constants.getDefaultPage());
                    break;
                }
                case CUSTOMIZED: {
                    Integer customizedPage = paginationDTO.getCustomizedPage();
                    List<Integer> defaultPages = Arrays.asList(constants.getDefaultPages());
                    if (defaultPages.contains(customizedPage)) {
                        employees = getEmployees(searchEmployee, paginationDTO.getPage(), customizedPage);
                    } else {
                        throw new ServiceEmployeeException();
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + paginationDTO.getPagination());
            }
        } catch (NullPointerException | IllegalStateException e) {
            employees = employeeRepository.getAll(null, searchEmployee);
        }
        if (!employees.isEmpty()) {
            return employees.stream()
                    .map(employeeConvertor::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Page<Employee> getEmployees(SearchEmployee searchEmployee, Integer page, Integer customizedPage) {
        Pageable pageable = PageRequest.of(page, customizedPage);
        return employeeRepository.getAll(pageable, searchEmployee);
    }

    private SearchEmployee convert(SearchDTO searchDTO) {
        SearchEmployee searchEmployee = new SearchEmployee();
        String legalName = searchDTO.getLegalName();
        String unp = searchDTO.getUnp();

        if (legalName != null || unp != null) {
            ResponseEntity<Object> responseEntity = getService.getLegalByNameAndUnp(legalName, unp);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List entityBody = (List) responseEntity.getBody();
                assert entityBody != null;
                List<Long> legalIds = new ArrayList<>();
                for (Object legal : entityBody) {
                    Map<String, Object> body = (Map<String, Object>) legal;
                    long legalId = Long.parseLong(body.get("LegalId").toString());
                    legalIds.add(legalId);
                }
                searchEmployee.setLegalId(legalIds);
            } else {
                throw new NoResultException("Компания по заданным параметрам не найдена");
            }
        }

        String fullNameIndividual = searchDTO.getFullNameIndividual();
        searchEmployee.setFullNameIndividual(fullNameIndividual);
        return searchEmployee;
    }
}