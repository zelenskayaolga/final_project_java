package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwt;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.*;
import com.zelenskaya.nestserava.app.service.model.AttemptsDTO;
import com.zelenskaya.nestserava.app.service.model.AuthDTO;
import com.zelenskaya.nestserava.app.service.model.LoginDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import com.zelenskaya.nestserava.app.service.validator.UsernameUsermailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    private static final String MAPPING_VERSION = "/api/v2";
    private static final String AUTH_LOGIN_URI = "/auth/login";

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
    private ValidatorAuthenticationService validatorAuthenticationService;
    @MockBean
    private AddSessionService addSessionService;
    @MockBean
    private UpdateAttemptsService updateAttemptsService;
    @MockBean
    private BlockUserService blockUserService;
    @MockBean
    private MessageSource messageSource;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private IsActiveUserService isActiveUserService;
    @MockBean
    private IsActiveSessionService isActiveSessionService;
    @MockBean
    private UpdateUserDetailsService updateUserDetailsService;
    @MockBean
    private SelectUserByUsernameService selectUserByUsernameService;

    private UsernameUsermailValidator usernameUsermailValidator = new UsernameUsermailValidator(
            selectUserByUsernameService
    );

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        mockMvc.perform(delete(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_ATOM_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn200WhenWePostValidUser() throws Exception {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxOTAxfQ.E" +
                "qAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("maikoku");
        loginDTO.setPassword("Maiko11!K");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(userDTO);
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(true);
        when(jwtUtils.generateJwtToken(loginDTO.getUsername(), userDTO.getId())).thenReturn(jwt);
        AuthDTO session = new AuthDTO();
        session.setSessionId(jwt);
        when(addSessionService.addWithUsername(jwt, loginDTO.getUsername())).thenReturn(session);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenWePostUserWithNullLogin() throws Exception {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxOTAxfQ.E" +
                "qAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(null);
        loginDTO.setPassword("Maiko11!K");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(userDTO);
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(true);
        when(jwtUtils.generateJwtToken(loginDTO.getUsername(), userDTO.getId())).thenReturn(jwt);
        AuthDTO session = new AuthDTO();
        session.setSessionId(jwt);
        when(addSessionService.addWithUsername(jwt, loginDTO.getUsername())).thenReturn(session);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWePostUserWithBlankLogin() throws Exception {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxOTAxfQ.E" +
                "qAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(" ");
        loginDTO.setPassword("Maiko11!K");

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(null);
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(false);
        AuthDTO session = new AuthDTO();
        session.setSessionId(jwt);
        when(addSessionService.addWithUsername(jwt, loginDTO.getUsername())).thenReturn(session);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenWePostUserWithLoginLength100Elements() throws Exception {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxOTAxfQ.E" +
                "qAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("m".repeat(100));
        loginDTO.setPassword("Maiko11!K");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(userDTO);
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(true);
        when(jwtUtils.generateJwtToken(loginDTO.getUsername(), userDTO.getId())).thenReturn(jwt);
        AuthDTO session = new AuthDTO();
        session.setSessionId(jwt);
        when(addSessionService.addWithUsername(jwt, loginDTO.getUsername())).thenReturn(session);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenWePostUserWithLoginLengthLongerThan100Elements() throws Exception {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYmJqYmJibCIsImlhdCI6MTY0Njg2MzkwMSwiZXhwIjoxNjQ2ODcxOTAxfQ.E" +
                "qAFswsvA3G_2L7Xq0xQJDAr-oF_cJjH_vpctF7pu9Yk3UJYds4PY5hQdGTPOPf3QyiBeffsJe4-yYwo9vPZxg";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("m".repeat(101));
        loginDTO.setPassword("Maiko11!K");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(userDTO);
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(true);
        when(jwtUtils.generateJwtToken(loginDTO.getUsername(), userDTO.getId())).thenReturn(jwt);
        AuthDTO session = new AuthDTO();
        session.setSessionId(jwt);
        when(addSessionService.addWithUsername(jwt, loginDTO.getUsername())).thenReturn(session);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWePostInvalidUserByUsernameAndQuantityOfAttemptsIsNull() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("maikoku");
        loginDTO.setPassword("Maiko11!K");

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(new UserDTO());
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(false);
        when(updateAttemptsService.updateByUsername(loginDTO.getUsername())).thenReturn(null);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWePostValidUserByUsernameAndQuantityOfAttemptsIs5() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("maikoku");
        loginDTO.setPassword("Maiko11!K");

        AttemptsDTO attemptsDTO = new AttemptsDTO();
        attemptsDTO.setQuantity(5);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(new UserDTO());
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(false);
        when(updateAttemptsService.updateByUsername(loginDTO.getUsername())).thenReturn(attemptsDTO);
        when(blockUserService.blockByUsername(loginDTO.getUsername())).thenReturn(new UserDTO());
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenWePostValidUserByUsernameAndQuantityOfAttemptsIsMoreThan5() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("maikoku");
        loginDTO.setPassword("Maiko11!K");

        AttemptsDTO attemptsDTO = new AttemptsDTO();
        attemptsDTO.setQuantity(6);

        when(selectUserByUsernameService.findByUsername(loginDTO.getUsername())).thenReturn(new UserDTO());
        when(validatorAuthenticationService.isValidUsername(loginDTO.getUsername(), loginDTO.getPassword()))
                .thenReturn(false);
        when(updateAttemptsService.updateByUsername(loginDTO.getUsername())).thenReturn(attemptsDTO);
        when(blockUserService.blockByUsername(loginDTO.getUsername())).thenReturn(new UserDTO());
        mockMvc.perform(post(MAPPING_VERSION + AUTH_LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }
}