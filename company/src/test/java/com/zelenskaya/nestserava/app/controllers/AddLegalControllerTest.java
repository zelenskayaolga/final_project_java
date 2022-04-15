package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwtCompany;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtilsCompany;
import com.zelenskaya.nestserava.app.service.AddLegalService;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
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

import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddLegalController.class)
class AddLegalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddLegalService addLegalService;
    @MockBean
    private LegalControllerConstants constants;
    @MockBean
    private AuthEntryPointJwtCompany authEntryPointJwt;
    @MockBean
    private JwtUtilsCompany jwtUtils;
    @MockBean
    private PostJwtService postJwtService;
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
    void shouldReturn201WhenWePostValidInput() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForPost() throws Exception {
        mockMvc.perform(post(WRONG_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        mockMvc.perform(delete(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn400WhenNameLegalIsBlank() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(" ");
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPostWithoutNameLegal() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenNameLegalLengthIs1Element() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal("С");
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn200WhenNameLegalLengthIs255Elements() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal("С".repeat(255));
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenNameLegalLengthIsLongerThan255Elements() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal("С".repeat(256));
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenUnpIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal("С".repeat(256));
        legalDTO.setUnp(null);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenUnpValueIs100000000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenUnpValueIsLessThan100000000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(99999999);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenUnpValueIsMoreThan799999999() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(800000000);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenUnpValueIs799999999() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenPostWithoutIban() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
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
    void shouldReturn400WhenIbanDoesNotMatchWithPattern(String iban) throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(iban);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTypeLegalIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(null);
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTotalEmployeesIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(null);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenTotalEmployeesNumberIs1000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenTotalEmployeesNumberIsMoreThan1000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(1001);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnMessageWhenNameLegalIsBlank() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(" ");
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenPostWithoutNameLegal() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenNameLegalIsLongerThan256Elements() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal("C".repeat(256));
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenUnpIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(null);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenUnpIsLessThan100000000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(99999999);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenPostWithoutIban() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
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
    void shouldReturnMessageWhenIbanDoesNotMatchPattern(String iban) throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(iban);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenTypeLegalIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(null);
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenTotalEmployeesIsNull() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(null);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMessageWhenTotalEmployeesIsMoreThan1000() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(1001);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage400()).thenReturn(MESSAGE_400);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_400, errors);
    }

    @Test
    void shouldReturnMapsToBusinessModelWhenNameLegalIsBlank() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(" ");
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(addLegalService, times(0)).add(legalDTO);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        LegalDTO result = objectMapper.readValue(actualResponseBody, LegalDTO.class);
        Assertions.assertNull(result.getNameLegal());
    }

    @Test
    void shouldReturnMessageWhenWePostValidInput() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenReturn(legalDTO);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage201()).thenReturn(MESSAGE_201);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_201, errors);
    }

    @Test
    void shouldReturn409WhenWePostConflictInput() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenThrow(ServiceLegalException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnMessageWhenWePostConflictInput() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenThrow(ServiceLegalException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage409()).thenReturn(MESSAGE_409);
        when(constants.getMessage409Separator()).thenReturn(MESSAGE_409_SEPARATOR);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isConflict())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_409 + legalDTO.getNameLegal()
                + MESSAGE_409_SEPARATOR + legalDTO.getUnp()
                + MESSAGE_409_SEPARATOR + legalDTO.getIban(), errors);
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

        when(addLegalService.add(legalDTO)).thenThrow(RuntimeException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnMessageWhenServerError() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(addLegalService.add(legalDTO)).thenThrow(RuntimeException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        when(constants.getMessage500()).thenReturn(MESSAGE_500);
        MvcResult mvcResult = mockMvc.perform(post(RIGHT_URL_TEMPLATE)
                        .header(HEADER_NAME, HEADER_VALUES + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(legalDTO)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals(MESSAGE_500, errors);
    }
}