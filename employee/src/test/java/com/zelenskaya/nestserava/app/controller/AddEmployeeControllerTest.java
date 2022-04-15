package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtEmployee;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsEmployee;
import com.zelenskaya.nestserava.app.service.AddEmployeeService;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.exception.ServiceExceptionConflict;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.AddedEmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.zelenskaya.nestserava.app.controller.EmployeeControllerTestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddEmployeeController.class)
class AddEmployeeControllerTest {

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
    private AddEmployeeService addEmployeeService;
    @MockBean
    private JwtUtilsConfig jwtUtilsConfig;

    @BeforeEach
    void preConfig() {
        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(HEADER_VALUES);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(SERVICE_JWT_TOKEN);
        when(jwtUtils.validateJwtToken(JWT)).thenReturn(true);
        when(jwtUtils.getIdFromJwtToken(JWT)).thenReturn(AUTHORIZED_USER_ID);
        ResponseEntity<Object> status = new ResponseEntity<>(HttpStatus.OK);
        when(postJwtService.status(JWT)).thenReturn(status);
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        ResponseEntity<Object> status = new ResponseEntity<>(HttpStatus.OK);

        when(jwtUtils.validateJwtToken(JWT)).thenReturn(true);
        when(jwtUtils.getIdFromJwtToken(JWT)).thenReturn(AUTHORIZED_USER_ID);
        when(postJwtService.status(JWT)).thenReturn(status);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForPost() throws Exception {
        ResponseEntity<Object> status = new ResponseEntity<>(HttpStatus.OK);

        when(jwtUtils.validateJwtToken(JWT)).thenReturn(true);
        when(jwtUtils.getIdFromJwtToken(JWT)).thenReturn(AUTHORIZED_USER_ID);
        when(postJwtService.status(JWT)).thenReturn(status);
        mockMvc.perform(post(WRONG_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        mockMvc.perform(delete(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(" ");
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenRecruitmentDateIsNull() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(null);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTerminationDateIsNull() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(null);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenNameLegalIsBlank() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(" ");
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenIbanBynIsNull() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(null);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenIbanCurrencyIsNull() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(null);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "00UNBS00000000000000000000", "by00UNBS00000000000000000000",
            "LT00UNBS00000000000000000000", "!!00UNBS00000000000000000000", "1100UNBS00000000000000000000",
            "0000UNBS00000000000000000000", "BY00!!!!00000000000000000000", "BY00111100000000000000000000",
            "BY00unbs00000000000000000000", "BY0000000000000000000000", "BYBYUNBS00000000000000000000",
            "BYbyUNBS00000000000000000000", "BYUNBS00000000000000000000", "BY!!UNBS00000000000000000000",
            "BY000UNBS00000000000000000000", "BY0UNBS00000000000000000000", "BY00UNBS0000000000000000000u",
            "BY00UNBS0000000000000000000!", "BY00UNBS0000000000000000000u", "BY00UNBS", "BY00UNBS000000000000000000001",
            "BY00UNBS00000000 00000000000"})
    void shouldReturn400WhenIbanBynDoesNotMatchPattern(String test) throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(test);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "00UNBS00000000000000000000", "by00UNBS00000000000000000000",
            "!!00UNBS00000000000000000000", "1100UNBS00000000000000000000", "0000UNBS00000000000000000000",
            "LT00!!!!00000000000000000000", "LT00111100000000000000000000", "LT00unbs00000000000000000000",
            "BY0000000000000000000000", "LTBYUNBS00000000000000000000", "LTbyUNBS00000000000000000000",
            "BYUNBS00000000000000000000", "LT!!UNBS00000000000000000000", "BY000UNBS00000000000000000000",
            "BY0UNBS00000000000000000000", "LT00UNBS0000000000000000000u", "LT00UNBS0000000000000000000!",
            "LT00UNBS0000000000000000000u", "LT00UNBS", "LT00UNBS000000000000000000001",
            "LT00UNBS00000000 00000000000"})
    void shouldReturn400WhenIbanCurrencyDoesNotMatchPattern(String test) throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(test);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenWePostNotUniqueUser() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        when(addEmployeeService.add(employeeDTO)).thenThrow(new ServiceExceptionConflict(
                "Сотрудник существует"
        ));
        mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldMapsToBusinessModelWhenWePostValidEmployee() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andReturn();
        verify(addEmployeeService, times(1)).add(employeeDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> result = objectMapper.readValue(actualResponseBody, Map.class);
        Assertions.assertTrue(result.containsKey("Сотрудник успешно создан"));
    }

    @Test
    void shouldMapsToBusinessModelWhenWePostInvalidEmployee() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(" ");
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(addEmployeeService, times(0)).add(employeeDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, String> result = objectMapper.readValue(actualResponseBody, Map.class);
        Assertions.assertTrue(result.containsValue("Неверно заданы параметры"));
    }

    @Test
    void shouldReturnUserWhenWePostValidInput() throws Exception {
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setName(RIGHT_NAME_EMPLOYEE);
        employeeDTO.setRecruitmentDate(RIGHT_RECRUITMENT_DATE);
        employeeDTO.setNameLegal(RIGHT_NAME_LEGAL);
        employeeDTO.setTerminationDate(RIGHT_TERMINATION_DATE);
        employeeDTO.setIbanByn(RIGHT_IBAN_BYN);
        employeeDTO.setIbanCurrency(RIGHT_IBAN_CURRENCY);

        AddedEmployeeDTO addedEmployeeDTO = new AddedEmployeeDTO();
        addedEmployeeDTO.setId(RIGHT_ID);

        when(addEmployeeService.add(employeeDTO)).thenReturn(addedEmployeeDTO);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_FOR_ADD_EMPLOYEES)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Map.of(
                "Сотрудник успешно создан", addedEmployeeDTO
        )));
    }
}