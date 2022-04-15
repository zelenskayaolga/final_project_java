package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectEmployeeByNameServiceImplTest {

    @InjectMocks
    private SelectEmployeeByNameServiceImpl selectEmployeeByNameServiceImpl;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeConvertor employeeConvertor;

    @Test
    void shouldReturnEmployeeByName() {
        Employee employee = new Employee();
        employee.setId(RIGHT_ID);
        employee.setName(RIGHT_NAME_EMPLOYEE);
        employee.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employee.setTerminationDate(RIGHT_TERMINATION_DATE);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        addEmployeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        addEmployeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        addEmployeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        addEmployeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        addEmployeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        List<AddEmployeeDTO> employeesDTO = new ArrayList<>();
        employeesDTO.add(addEmployeeDTO);

        when(employeeRepository.getByFullName(RIGHT_NAME_EMPLOYEE)).thenReturn(employees);
        when(employeeConvertor.convertToDTO(employee)).thenReturn(addEmployeeDTO);
        List<AddEmployeeDTO> employeeDTO = selectEmployeeByNameServiceImpl.getByFullNameIndividual(RIGHT_NAME_EMPLOYEE);
        Assertions.assertNotNull(employeeDTO);
    }
}