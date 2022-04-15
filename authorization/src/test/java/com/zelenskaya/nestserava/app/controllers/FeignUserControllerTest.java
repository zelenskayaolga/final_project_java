package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwt;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.IsActiveSessionService;
import com.zelenskaya.nestserava.app.service.IsActiveUserService;
import com.zelenskaya.nestserava.app.service.SelectUserByIdService;
import com.zelenskaya.nestserava.app.service.UpdateUserDetailsService;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FeignUserController.class)
class FeignUserControllerTest {

    private static final String MAPPING_VERSION = "/api/v2";
    private static final String USER_BY_ID_URI = "/user/{userId}";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private JwtUtilsConfig jwtUtilsConfig;
    @MockBean
    private SelectUserByIdService selectUserByIdService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private IsActiveUserService isActiveUserService;
    @MockBean
    private IsActiveSessionService isActiveSessionService;
    @MockBean
    private UpdateUserDetailsService updateUserDetailsService;

    @Test
    void shouldReturn200WhenValidGet() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        Long userId = 1L;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(selectUserByIdService.findById(userId)).thenReturn(userDTO);
        mockMvc.perform(get(MAPPING_VERSION + USER_BY_ID_URI, userId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn415WhenWeGetWithXmlContentType() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        mockMvc.perform(get(MAPPING_VERSION + USER_BY_ID_URI, 1L)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_ATOM_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForGet() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        mockMvc.perform(delete(MAPPING_VERSION + USER_BY_ID_URI, 1L)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn404WhenInvalidUrlForGet() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        mockMvc.perform(get(MAPPING_VERSION + "/legal/{legalId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldMapsToBusinessModelWhenValidGet() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        Long userId = 1L;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(selectUserByIdService.findById(userId)).thenReturn(userDTO);
        MvcResult mvcResult = mockMvc.perform(get(MAPPING_VERSION + USER_BY_ID_URI, userId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        verify(selectUserByIdService, times(1)).findById(userId);
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserDTO result = objectMapper.readValue(actualResponseBody, UserDTO.class);
        Assertions.assertEquals(userDTO, result);
    }

    @Test
    void shouldReturnUserWhenValidGet() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";
        String serviceJwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6ZWxlbnNrYXlva2siLCJ1c2V" +
                "ySWQiOjEsImlhdCI6MTY0NzYxNDgyNywiZXhwIjoxNjQ3ODc0MDI3fQ.BFrMotMqSoiTNNRx_" +
                "PuVJfH15GElcI7zY-LX-X6jTojJtFyhlkB-JkJaw5j86iEUcPcQ0QSS15ZiZ7_aS03vxg";

        Long userId = 1L;

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        when(jwtUtilsConfig.getBearerPrefix()).thenReturn(BEARER_PREFIX);
        when(jwtUtilsConfig.getServiceJwtToken()).thenReturn(serviceJwtToken);
        when(selectUserByIdService.findById(userId)).thenReturn(userDTO);
        MvcResult mvcResult = mockMvc.perform(get(MAPPING_VERSION + USER_BY_ID_URI, userId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(userDTO));
    }
}