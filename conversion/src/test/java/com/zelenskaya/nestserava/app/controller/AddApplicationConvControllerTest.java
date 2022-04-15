package com.zelenskaya.nestserava.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilConfig;
import com.zelenskaya.nestserava.app.controller.security.filter.AuthTokenFilter;
import com.zelenskaya.nestserava.app.controller.security.filter.ParseJwt;
import com.zelenskaya.nestserava.app.controller.security.point.AuthEntryPointJwtConversion;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsConversion;
import com.zelenskaya.nestserava.app.service.CvsAddService;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.model.AddedApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.util.CvsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zelenskaya.nestserava.app.controller.ApplicationConvTestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AddApplicationConvController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddApplicationConvControllerTest {
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
    private CvsUtil cvsUtil;
    @MockBean
    private CvsAddService cvsService;
    @MockBean
    private JwtUtilConfig jwtUtilsConfig;
    @MockBean
    private ParseJwt parseJwt;

    @Test
    void shouldReturn200WhenIsMvcFormat() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                NAME_FILE,
                APPLICATION_CONV_CVS,
                MULTIPART_FORM_DATA_VALUE,
                CVS_DATA.getBytes(StandardCharsets.UTF_8));
        when(cvsUtil.isCSVFormat(mockMultipartFile)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.multipart(ADD_URL_TEMPLATE)
                        .file(mockMultipartFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenIsNotMvcFormat() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                NAME_FILE,
                APPLICATION_CONV_CVS,
                MULTIPART_FORM_DATA_VALUE,
                CVS_DATA.getBytes(StandardCharsets.UTF_8));
        when(cvsUtil.isCSVFormat(mockMultipartFile)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.multipart(ADD_URL_TEMPLATE)
                        .file(mockMultipartFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnMessageWhenIsNotMvcFormat() throws Exception {
        List<AddedApplicationConvDTO> applicationConvDTOList = new ArrayList<>();
        AddedApplicationConvDTO addedApplicationConvDTO = new AddedApplicationConvDTO();
        addedApplicationConvDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        applicationConvDTOList.add(addedApplicationConvDTO);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                NAME_FILE,
                APPLICATION_CONV_CVS,
                MULTIPART_FORM_DATA_VALUE,
                CVS_DATA.getBytes(StandardCharsets.UTF_8));
        when(cvsUtil.isCSVFormat(mockMultipartFile)).thenReturn(false);
        when(cvsService.addApplicationConv(mockMultipartFile)).thenReturn(applicationConvDTOList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(ADD_URL_TEMPLATE)
                        .file(mockMultipartFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        String errors = (String) map.get(MESSAGE_KEY);
        Assertions.assertEquals("Недопустимый формат файла", errors);
    }

    @Test
    void shouldReturn200WhenValidInput() throws Exception {
        List<AddedApplicationConvDTO> applicationConvDTOList = new ArrayList<>();
        AddedApplicationConvDTO addedApplicationConvDTO = new AddedApplicationConvDTO();
        addedApplicationConvDTO.setApplicationConvId(APPLICATION_CONV_UUID);
        applicationConvDTOList.add(addedApplicationConvDTO);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                NAME_FILE,
                APPLICATION_CONV_CVS,
                MULTIPART_FORM_DATA_VALUE,
                CVS_DATA.getBytes(StandardCharsets.UTF_8));
        when(cvsUtil.isCSVFormat(mockMultipartFile)).thenReturn(true);
        when(cvsService.addApplicationConv(mockMultipartFile)).thenReturn(applicationConvDTOList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(ADD_URL_TEMPLATE)
                        .file(mockMultipartFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Map.of(
                MESSAGE_KEY, "Сохранено " + applicationConvDTOList.size() + " заявок"
        )));
    }

    @Test
    void shouldReturn400WhenIllegalArgumentException() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                NAME_FILE,
                APPLICATION_CONV_CVS,
                MULTIPART_FORM_DATA_VALUE,
                CVS_DATA.getBytes(StandardCharsets.UTF_8));
        when(cvsUtil.isCSVFormat(mockMultipartFile)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(MockMvcRequestBuilders.multipart(ADD_URL_TEMPLATE)
                        .file(mockMultipartFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }
}