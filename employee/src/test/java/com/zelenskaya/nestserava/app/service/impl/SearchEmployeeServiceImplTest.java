package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.SearchEmployee;
import com.zelenskaya.nestserava.app.service.GetLegalByNameAndUnpService;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationEnumDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.FULL_NAME_INDIVIDUAL;
import static com.zelenskaya.nestserava.app.service.impl.EmployeeServiceTestConstants.RIGHT_UNP_LEGAL;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchEmployeeServiceImplTest {

    @InjectMocks
    private SearchEmployeeServiceImpl searchEmployeeService;

    @Mock
    private ServiceEmployeeConstants constants;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeConvertor employeeConvertor;
    @Mock
    private GetLegalByNameAndUnpService getService;

    @Test
    void shouldReturnListOfAddEmployeeDTO() {
        Pageable pageable = PageRequest.of(1, 10);

        String legalName = "name";
        String unp = String.valueOf(RIGHT_UNP_LEGAL);
        String legalId = "LegalId";
        Long id = 1L;

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(PaginationEnumDTO.DEFAULT);
        paginationDTO.setPage(1);

        SearchEmployee searchEmployee = new SearchEmployee();
        searchEmployee.setLegalId(Collections.singletonList(id));
        searchEmployee.setFullNameIndividual(FULL_NAME_INDIVIDUAL);

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setLegalName(legalName);
        searchDTO.setUnp(String.valueOf(RIGHT_UNP_LEGAL));
        searchDTO.setFullNameIndividual(FULL_NAME_INDIVIDUAL);

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK)
                .body(List.of(Map.of(legalName, "name", unp, RIGHT_UNP_LEGAL, legalId, id)));

        Page<Employee> employees = new PageImpl<>(Collections.emptyList());
        when(constants.getDefaultPage()).thenReturn(10);
        when(getService.getLegalByNameAndUnp(legalName, unp)).thenReturn(response);
        when(employeeRepository.getAll(pageable, searchEmployee)).thenReturn(employees);
        List<AddEmployeeDTO> addEmployeesDTO = searchEmployeeService.get(searchDTO, paginationDTO);
        Assertions.assertEquals(Collections.emptyList(), addEmployeesDTO);
    }
}