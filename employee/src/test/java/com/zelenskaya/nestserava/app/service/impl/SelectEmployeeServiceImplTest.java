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

import javax.persistence.NoResultException;

import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectEmployeeServiceImplTest {

    @InjectMocks
    private SelectEmployeeServiceImpl selectEmployeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeConvertor employeeConvertor;

    @Test
    void shouldReturnEmployeeDTOWhenWeGetItById() {
        Employee employee = new Employee();
        employee.setId(RIGHT_ID);
        employee.setName(RIGHT_NAME_EMPLOYEE);
        employee.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employee.setTerminationDate(RIGHT_TERMINATION_DATE);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        when(employeeRepository.findById(employee.getId())).thenReturn(employee);
        when(employeeConvertor.convertToDTO(employee)).thenReturn(employeeDTO);
        AddEmployeeDTO addedEmployeeDTO = selectEmployeeService.getById(RIGHT_ID);
        Assertions.assertEquals(employeeDTO, addedEmployeeDTO);
    }

    @Test
    void shouldReturnExceptionWhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(RIGHT_ID)).thenReturn(null);
        NoResultException exception = Assertions.assertThrows(
                NoResultException.class, () -> selectEmployeeService.getById(RIGHT_ID)
        );
        Assertions.assertEquals("Сотрудника не существует", exception.getMessage());
    }
}
