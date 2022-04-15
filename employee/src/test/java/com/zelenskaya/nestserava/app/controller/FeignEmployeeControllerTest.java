package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtEmployee;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsEmployee;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.SelectEmployeeByNameService;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.zelenskaya.nestserava.app.controller.EmployeeControllerTestConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FeignEmployeeController.class)
class FeignEmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthEntryPointJwtEmployee authEntryPointJwt;
    @MockBean
    private JwtUtilsEmployee jwtUtils;
    @MockBean
    private PostJwtService postJwtService;
    @MockBean
    private SelectEmployeeByNameService selectEmployeeByNameService;
    @MockBean
    private JwtUtilsConfig jwtUtilsConfig;

    @Test
    @WithMockUser
    void shouldReturn200WhenWeGetEmployeeDTOById() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setId(RIGHT_ID);
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        when(selectEmployeeByNameService.getByFullNameIndividual(RIGHT_NAME_EMPLOYEE))
                .thenReturn(Collections.singletonList(employeeDTO));
        mockMvc.perform(get(RIGHT_URL_FOR_FEIGN_EMPLOYEE)
                        .param("Full_Name_Individual", (RIGHT_NAME_EMPLOYEE))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBadRequest() throws Exception {
        when(selectEmployeeByNameService.getByFullNameIndividual("employee")).thenReturn(Collections.EMPTY_LIST);
        mockMvc.perform(get(RIGHT_URL_FOR_FEIGN_EMPLOYEE)
                        .param("Full_Name_Individual", "employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}