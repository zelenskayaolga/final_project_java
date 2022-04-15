package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeDetailsRepository;
import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.PositionLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.EmployeeDetails;
import com.zelenskaya.nestserava.app.repository.model.PositionEnum;
import com.zelenskaya.nestserava.app.repository.model.PositionLegal;
import com.zelenskaya.nestserava.app.service.GetLegalService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.config.ServiceConfig;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.AddedEmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddEmployeeServiceImplTest {

    @InjectMocks
    private AddEmployeeServiceImpl addEmployeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeDetailsRepository employeeDetailsRepository;
    @Mock
    private LocalDateService localDateService;
    @Mock
    private ServiceConfig serviceConfig;
    @Mock
    private GetLegalService getService;
    @Mock
    private PositionLegalRepository positionLegalRepository;

    @Test
    void shouldReturnAddedEmployeeDTOWhenValidInput() {
        Long id = 1L;
        String nameLegal = "Company";
        String legalId = "LegalId";

        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body(Map.of(
                nameLegal, "Company",
                legalId, id
        ));

        PositionLegal positionLegal = new PositionLegal();
        positionLegal.setId(id);
        positionLegal.setPosition(PositionEnum.valueOf("HIRED"));

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setCreateDate(LocalDate.now());
        employeeDetails.setUpdateDate(LocalDate.now());

        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);
        addEmployeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        addEmployeeDTO.setRecruitmentDate(LocalDate.now());
        addEmployeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        addEmployeeDTO.setNameLegal(nameLegal);
        addEmployeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        addEmployeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        Employee employee = new Employee();
        employee.setId(addEmployeeDTO.getId());
        employee.setName(addEmployeeDTO.getNameLegal());
        employee.setRecruitmentDate(LocalDate.now());
        employee.setTerminationDate(RIGHT_TERMINATION_DATE);
        employee.setLegalId(id);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);
        employee.setEmployeeDetails(employeeDetails);
        employee.setPosition(positionLegal);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(employee.getId());

        when(serviceConfig.getZoneTime()).thenReturn(TIME_ZONE);
        when(positionLegalRepository.findByPosition(PositionEnum.HIRED)).thenReturn(Optional.of(positionLegal));
        when(getService.getLegalByName(addEmployeeDTO.getNameLegal())).thenReturn(responseEntity);
        when(localDateService.dateTimeWithZone(TIME_ZONE)).thenReturn(LocalDate.now());
        AddedEmployeeDTO employeeDTO = addEmployeeService.add(addEmployeeDTO);
        Assertions.assertNotNull(employeeDTO);
    }

    @Test
    void shouldReturnExceptionWhenTerminationDateIsBeforeRecruitmentDate() {
        Long legalId = 1L;

        Employee employee = new Employee();
        employee.setId(RIGHT_ID);
        employee.setName(RIGHT_NAME_EMPLOYEE);
        employee.setRecruitmentDate(LocalDate.now());
        employee.setTerminationDate(LocalDate.now().minusDays(1));
        employee.setLegalId(legalId);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);
        addEmployeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        addEmployeeDTO.setRecruitmentDate(LocalDate.now());
        addEmployeeDTO.setTerminationDate(LocalDate.now().minusDays(1));
        addEmployeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        addEmployeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        addEmployeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);
        ServiceEmployeeException exception = Assertions.assertThrows(
                ServiceEmployeeException.class, () -> addEmployeeService.add(addEmployeeDTO)
        );
        Assertions.assertEquals("Введите правильную дату", exception.getMessage());
    }

    @Test
    void shouldReturnExceptionWhenRecruitmentDateIsNotEqualsLocalDateNow() {
        Long legalId = 1L;

        Employee employee = new Employee();
        employee.setId(RIGHT_ID);
        employee.setName(RIGHT_NAME_EMPLOYEE);
        employee.setRecruitmentDate(LocalDate.now().minusDays(1));
        employee.setTerminationDate(LocalDate.now());
        employee.setLegalId(legalId);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);
        addEmployeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        addEmployeeDTO.setRecruitmentDate(LocalDate.now().minusDays(1));
        addEmployeeDTO.setTerminationDate(LocalDate.now());
        addEmployeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        addEmployeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        addEmployeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        ServiceEmployeeException exception = Assertions.assertThrows(
                ServiceEmployeeException.class, () -> addEmployeeService.add(addEmployeeDTO)
        );
        Assertions.assertEquals("Введите правильную дату", exception.getMessage());
    }

    @Test
    void shouldReturnExceptionWhenLegalWithIdDoesNotExist() {
        Long legalId = 1L;

        Employee employee = new Employee();
        employee.setId(RIGHT_ID);
        employee.setName(RIGHT_NAME_EMPLOYEE);
        employee.setRecruitmentDate(LocalDate.now());
        employee.setTerminationDate(RIGHT_TERMINATION_DATE);
        employee.setLegalId(legalId);
        employee.setIbanByn(RIGHT_IBAN_BYN);
        employee.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);
        addEmployeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        addEmployeeDTO.setRecruitmentDate(LocalDate.now());
        addEmployeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        addEmployeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        addEmployeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        addEmployeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        ResponseEntity<Object> status = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(getService.getLegalByName(RIGHT_NAME_LEGAL)).thenReturn(status);
        NoResultException exception = Assertions.assertThrows(
                NoResultException.class, () -> addEmployeeService.add(addEmployeeDTO)
        );
        Assertions.assertEquals("Компания по заданным параметрам не найдена", exception.getMessage());
    }
}