package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtEmployee;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsEmployee;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.SearchEmployeeService;
import com.zelenskaya.nestserava.app.service.SelectEmployeeService;
import com.zelenskaya.nestserava.app.service.exception.ServiceEmployeeException;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static com.zelenskaya.nestserava.app.controller.EmployeeControllerTestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GetEmployeeController.class)
class GetEmployeeControllerTest {
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
    private SearchEmployeeService searchEmployeeService;
    @MockBean
    private SelectEmployeeService employeeService;
    @MockBean
    private JwtUtilsConfig jwtUtilsConfig;
    @MockBean
    private EmployeeControllerConstants employeeControllerConstants;

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
    void shouldReturn200WhenWeGetLegalById() throws Exception {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);

        when(employeeService.getById(RIGHT_ID)).thenReturn(addEmployeeDTO);
        mockMvc.perform(get(URL_BY_ID_EMPLOYEE, RIGHT_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn415WhenWeGetByIdWithXmlContentType() throws Exception {
        mockMvc.perform(get(URL_BY_ID_EMPLOYEE, RIGHT_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_ATOM_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForGetById() throws Exception {
        mockMvc.perform(post("/api/v2/legels/{id}", RIGHT_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForGetById() throws Exception {
        mockMvc.perform(delete(URL_BY_ID_EMPLOYEE, RIGHT_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnLegalWhenWeGetLegalById() throws Exception {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);

        when(employeeService.getById(RIGHT_ID)).thenReturn(addEmployeeDTO);
        MvcResult mvcResult = mockMvc.perform(get(URL_BY_ID_EMPLOYEE, RIGHT_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(addEmployeeDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace("[" + objectMapper.writeValueAsString(addEmployeeDTO) + "]");
    }

    @Test
    void shouldReturn200WhenWeGetLegalSearch() throws Exception {
        AddEmployeeDTO addEmployeeDTO = new AddEmployeeDTO();
        addEmployeeDTO.setId(RIGHT_ID);

        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchEmployeeService.get(searchDTO, paginationDTO)).thenReturn(List.of(addEmployeeDTO));
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenWeGetLegalSearchNotFound() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchEmployeeService.get(searchDTO, paginationDTO)).thenReturn(Collections.emptyList());
        when(employeeControllerConstants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(employeeControllerConstants.getMessage404NotFound()).thenReturn(MESSAGE_404_NOT_FOUND);
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenWeGetLegalSearchWithWrongCustomizedPagination() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchEmployeeService.get(searchDTO, paginationDTO)).thenThrow(ServiceEmployeeException.class);
        when(employeeControllerConstants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(employeeControllerConstants.getMessage400()).thenReturn(MESSAGE_400);
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(paginationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeGetSearchWithXmlContentType() throws Exception {
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForSearch() throws Exception {
        mockMvc.perform(delete(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn500WhenServerErrorFromSearch() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();
        when(searchEmployeeService.get(searchDTO, paginationDTO)).thenThrow(RuntimeException.class);
        when(employeeControllerConstants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn404WhenNotFoundLegalForSearch() throws Exception {
        when(employeeControllerConstants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(URL_EMPLOYEE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


}