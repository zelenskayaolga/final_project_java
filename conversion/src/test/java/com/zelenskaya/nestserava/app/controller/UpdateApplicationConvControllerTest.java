package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilConfig;
import com.zelenskaya.nestserava.app.controller.security.filter.AuthTokenFilter;
import com.zelenskaya.nestserava.app.controller.security.filter.ParseJwt;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtConversion;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsConversion;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.UpdateNameLegalService;
import com.zelenskaya.nestserava.app.service.UpdateStatusService;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UpdateNameLegalDTO;
import com.zelenskaya.nestserava.app.service.model.UpdateStatusDTO;
import com.zelenskaya.nestserava.app.service.model.UpdatedStatusDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.zelenskaya.nestserava.app.controller.ApplicationConvTestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UpdateApplicationConvController.class)
@AutoConfigureMockMvc(addFilters = false)
class UpdateApplicationConvControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthEntryPointJwtConversion authEntryPointJwt;
    @MockBean
    private AuthTokenFilter authTokenFilter;
    @MockBean
    private JwtUtilsConversion jwtUtils;
    @MockBean
    private PostJwtService postJwtService;
    @MockBean
    private UpdateNameLegalService updateNameLegalService;
    @MockBean
    private UpdateStatusService updateStatusService;
    @MockBean
    private JwtUtilConfig jwtUtilsConfig;
    @MockBean
    private ParseJwt parseJwt;

    @Test
    void shouldReturn200WhenValidInputForUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(APPLICATION_CONV_UUID);

        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        updatedStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updatedStatusDTO.setUsername(USERNAME);
        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(updatedStatusDTO);
        mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn405WhenPostForUpdateStatus() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenXmlTypeForUpdateStatus() throws Exception {
        mockMvc.perform(put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenBadUrlForUpdateStatus() throws Exception {
        mockMvc.perform(put(BAD_URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn500WhenServerErrorForUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(null);
        mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn400WhenInvalidUuidUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(INVALID_UUID);

        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        updatedStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updatedStatusDTO.setUsername(USERNAME);
        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(updatedStatusDTO);
        mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnMessageWhenServerErrorForUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals("Сервер не доступен", errors);
    }

    @Test
    void shouldReturnMapsToBusinessModelWhenUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(APPLICATION_CONV_UUID);

        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        updatedStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updatedStatusDTO.setUsername(USERNAME);

        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(updatedStatusDTO);

        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isOk()).andReturn();
        verify(updateStatusService, times(1))
                .update(eq(updateStatusDTO), any(HttpServletRequest.class));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        UpdatedStatusDTO result = objectMapper.readValue(actualResponseBody, UpdatedStatusDTO.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnMessageWhenInvalidUuidInUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setApplicationConvId(INVALID_UUID);

        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        updatedStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updatedStatusDTO.setUsername(USERNAME);

        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(updatedStatusDTO);

        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(Collections.singletonList("Введите ApplicationConvId в требуемом формате"), errors);
    }

    @Test
    void shouldReturnDTOWhenValidUpdateStatus() throws Exception {
        UpdateStatusDTO updateStatusDTO = new UpdateStatusDTO();
        updateStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updateStatusDTO.setApplicationConvId(APPLICATION_CONV_UUID);

        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        updatedStatusDTO.setStatus(StatusEnumDTO.IN_PROGRESS);
        updatedStatusDTO.setUsername(USERNAME);

        when(updateStatusService.update(eq(updateStatusDTO), any(HttpServletRequest.class)))
                .thenReturn(updatedStatusDTO);

        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateStatusDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(
                Map.of("Статус изменен", updatedStatusDTO)));
    }

    @Test
    void shouldReturn200WhenValidInputForUpdateById() throws Exception {
        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        updateNameLegalDTO.setNameLegal(NAME_LEGAL);
        when(updateNameLegalService.update(eq(APPLICATION_CONV_ID), eq(updateNameLegalDTO), any(HttpServletRequest.class)))
                .thenReturn(updateNameLegalDTO);

        mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateNameLegalDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn405WhenPostForUpdateById() throws Exception {
        mockMvc.perform(post(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenXmlTypeForUpdateById() throws Exception {
        mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenBadUrlForUpdateById() throws Exception {
        mockMvc.perform(put(BAD_URL_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnMessageWhenValidInputForUpdateById() throws Exception {
        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        updateNameLegalDTO.setNameLegal(NAME_LEGAL);
        when(updateNameLegalService.update(eq(APPLICATION_CONV_ID),
                eq(updateNameLegalDTO), any(HttpServletRequest.class)))
                .thenReturn(updateNameLegalDTO);

        MvcResult mvcResult = mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateNameLegalDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String message = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals("Заявка на конверсию " + updateNameLegalDTO.getApplicationConvId() +
                " перепривязана к OMA", message);
    }

    @Test
    void shouldReturnMessageWhenInvalidInputForUpdateById() throws Exception {
        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        updateNameLegalDTO.setNameLegal(NAME_LEGAL);
        when(updateNameLegalService.update(eq(APPLICATION_CONV_ID), eq(updateNameLegalDTO), any(HttpServletRequest.class)))
                .thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateNameLegalDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String message = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals("Заявка на конверсию " + updateNameLegalDTO.getApplicationConvId() +
                " привязана к OMA", message);
    }

    @Test
    void shouldReturnMapsToBusinessModelWhenValidInputUpdateById() throws Exception {
        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        updateNameLegalDTO.setNameLegal(NAME_LEGAL);
        when(updateNameLegalService.update(eq(APPLICATION_CONV_ID), eq(updateNameLegalDTO), any(HttpServletRequest.class)))
                .thenReturn(updateNameLegalDTO);

        MvcResult mvcResult = mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateNameLegalDTO)))
                .andExpect(status().isOk()).andReturn();
        verify(updateNameLegalService, times(1))
                .update(eq(APPLICATION_CONV_ID), eq(updateNameLegalDTO), any(HttpServletRequest.class));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        UpdateNameLegalDTO result = objectMapper.readValue(actualResponseBody, UpdateNameLegalDTO.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void shouldReturnUpdateNameLegalDTOWhenValidUpdateById() throws Exception {
        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        updateNameLegalDTO.setNameLegal(NAME_LEGAL);
        when(updateNameLegalService.update(eq(APPLICATION_CONV_ID), eq(updateNameLegalDTO), any(HttpServletRequest.class)))
                .thenReturn(updateNameLegalDTO);
        MvcResult mvcResult = mockMvc.perform(put(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(updateNameLegalDTO)))
                .andExpect(status().isOk()).andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(
                Map.of(MESSAGE_KEY, "Заявка на конверсию " + updateNameLegalDTO.getApplicationConvId() +
                        " перепривязана к " + updateNameLegalDTO.getNameLegal())));
    }
}