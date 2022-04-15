package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwtCompany;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtilsCompany;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.SearchLegalService;
import com.zelenskaya.nestserava.app.service.SelectLegalService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
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

import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.AUTHORIZED_USER_ID;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.HEADER_NAME;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.HEADER_VALUES;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.JSON_CONTENT_TYPE;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.JWT;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.MESSAGE_400;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.MESSAGE_404_NOT_FOUND;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.MESSAGE_KEY;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_IBAN_LEGAL;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_LEGAL_ID;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_NAME_LEGAL;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_TOTAL_EMPLOYEES;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_TYPE_LEGAL;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_UNP_LEGAL;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_URL_TEMPLATE;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.SERVICE_JWT_TOKEN;
import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.XML_CONTENT_TYPE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GetLegalController.class)
class GetLegalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthEntryPointJwtCompany authEntryPointJwt;
    @MockBean
    private JwtUtilsCompany jwtUtils;
    @MockBean
    private PostJwtService postJwtService;
    @MockBean
    private SearchLegalService searchService;
    @MockBean
    private SelectLegalService selectLegalService;
    @MockBean
    private LegalControllerConstants constants;
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
    void shouldReturn200WhenWeGetLegalById() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(selectLegalService.getById(RIGHT_LEGAL_ID)).thenReturn(legalDTO);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn415WhenWeGetByIdWithXmlContentType() throws Exception {
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_ATOM_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForGetById() throws Exception {
        mockMvc.perform(post("/api/v2/legels/{id}", RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForGetById() throws Exception {
        mockMvc.perform(delete(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn500WhenServerError() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(selectLegalService.getById(RIGHT_LEGAL_ID)).thenThrow(RuntimeException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn404WhenNotFoundLegalForGetById() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnLegalWhenWeGetLegalById() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf("RESIDENT"));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(selectLegalService.getById(RIGHT_LEGAL_ID)).thenReturn(legalDTO);
        MvcResult mvcResult = mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_GET_LEGAL_CONTROLLER, RIGHT_LEGAL_ID)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace("[" + objectMapper.writeValueAsString(legalDTO) + "]");
    }

    @Test
    void shouldReturn200WhenWeGetLegalSearch() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(1L);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchService.get(searchDTO, paginationDTO)).thenReturn(List.of(legalDTO));
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenWeGetLegalSearchNotFound() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchService.get(searchDTO, paginationDTO)).thenReturn(Collections.emptyList());
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage404NotFound()).thenReturn(MESSAGE_404_NOT_FOUND);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenWeGetLegalSearchWithWrongCustomizedPagination() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();

        when(searchService.get(searchDTO, paginationDTO)).thenThrow(ServiceLegalException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(paginationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn415WhenWeGetSearchWithXmlContentType() throws Exception {
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForSearch() throws Exception {
        mockMvc.perform(delete(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn500WhenServerErrorFromSearch() throws Exception {
        SearchDTO searchDTO = new SearchDTO();
        PaginationDTO paginationDTO = new PaginationDTO();
        when(searchService.get(searchDTO, paginationDTO)).thenThrow(RuntimeException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn404WhenNotFoundLegalForSearch() throws Exception {
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}