package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilConfig;
import com.zelenskaya.nestserava.app.controller.security.filter.AuthTokenFilter;
import com.zelenskaya.nestserava.app.controller.security.filter.ParseJwt;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtConversion;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsConversion;
import com.zelenskaya.nestserava.app.service.GetApplicationConvService;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceConversionException;
import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
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

import java.util.Collections;
import java.util.List;

import static com.zelenskaya.nestserava.app.controller.ApplicationConvTestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GetApplicationConvController.class)
@AutoConfigureMockMvc(addFilters = false)
class GetApplicationConvControllerTest {
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
    private JwtUtilConfig jwtUtilsConfig;
    @MockBean
    private ParseJwt parseJwt;
    @MockBean
    private ConversionControllerConstants constants;
    @MockBean
    private GetApplicationConvService getApplicationConvService;

    @Test
    void shouldReturn200WhenWeGetById() throws Exception {
        ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();
        applicationConvDTO.setId(APPLICATION_CONV_ID);
        when(getApplicationConvService.getById(APPLICATION_CONV_ID)).thenReturn(applicationConvDTO);
        mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn405WhenPostForGetById() throws Exception {
        mockMvc.perform(post(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenXmlTypeForGetById() throws Exception {
        mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenBadUrlForGetById() throws Exception {
        mockMvc.perform(get(BAD_URL_TEMPLATE, APPLICATION_CONV_ID)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn500WhenWeGetById() throws Exception {
        when(getApplicationConvService.getById(APPLICATION_CONV_ID)).thenReturn(null);
        mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn500WhenThrowRuntimeExceptionGetById() throws Exception {
        when(getApplicationConvService.getById(APPLICATION_CONV_ID)).thenThrow(RuntimeException.class);
        mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnApplicationConvWhenWeGetById() throws Exception {
        ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();
        applicationConvDTO.setId(APPLICATION_CONV_ID);
        when(getApplicationConvService.getById(APPLICATION_CONV_ID)).thenReturn(applicationConvDTO);
        MvcResult mvcResult = mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(applicationConvDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace("[" + objectMapper.writeValueAsString(applicationConvDTO) + "]");
    }

    @Test
    void shouldReturnMapsToBusinessModelWhenWeGetById() throws Exception {
        ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();
        applicationConvDTO.setId(APPLICATION_CONV_ID);
        when(getApplicationConvService.getById(APPLICATION_CONV_ID)).thenReturn(applicationConvDTO);
        MvcResult mvcResult = mockMvc.perform(get(URL_BY_ID_TEMPLATE, APPLICATION_CONV_ID)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(applicationConvDTO)))
                .andExpect(status().isOk())
                .andReturn();
        verify(getApplicationConvService, times(1)).getById(APPLICATION_CONV_ID);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<ApplicationConvDTO> result = objectMapper.readValue(actualResponseBody, List.class);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturn200WhenWeGetPagination() throws Exception {
        ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();
        applicationConvDTO.setId(APPLICATION_CONV_ID);
        PaginationDTO paginationDTO = new PaginationDTO();
        when(getApplicationConvService.get(paginationDTO)).thenReturn(List.of(applicationConvDTO));
        mockMvc.perform(get(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn405WhenPostForGetPagination() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenXmlTypeForGetPagination() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(XML_CONTENT_TYPE))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn404WhenBadUrlForGetPagination() throws Exception {
        mockMvc.perform(get(BAD_URL_TEMPLATE)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn500WhenWeGetPagination() throws Exception {
        PaginationDTO paginationDTO = new PaginationDTO();
        when(getApplicationConvService.get(paginationDTO)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn500WhenRuntimeExceptionGetPagination() throws Exception {
        PaginationDTO paginationDTO = new PaginationDTO();
        when(getApplicationConvService.get(paginationDTO)).thenThrow(RuntimeException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturn404WhenNotFoundGetPagination() throws Exception {
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenServiceConversionException() throws Exception {
        PaginationDTO paginationDTO = new PaginationDTO();
        when(getApplicationConvService.get(paginationDTO)).thenThrow(ServiceConversionException.class);
        when(constants.getMessageKey()).thenReturn(MESSAGE_KEY);
        mockMvc.perform(get(URL_TEMPLATE)
                        .header(HEADER_NAME, BEARER_PREFIX + JWT)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isBadRequest());
    }
}