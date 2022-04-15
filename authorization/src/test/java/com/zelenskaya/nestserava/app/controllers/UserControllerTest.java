package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwt;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtils;
import com.zelenskaya.nestserava.app.service.*;
import com.zelenskaya.nestserava.app.service.model.AddedUserDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private static final String MAPPING_VERSION = "/api/v2";
    private static final String AUTH_SIGNIN_URI = "/auth/signin";
    private static final String ERRORS_KEY = "errors";
    private static final String MESSAGE_KEY = "message";

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
    private AddUserService addUserService;
    @MockBean
    private ValidatorUsernameService validatorUsernameService;
    @MockBean
    private ValidatorUserMailService validatorUserMailService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private IsActiveUserService isActiveUserService;
    @MockBean
    private IsActiveSessionService isActiveSessionService;
    @MockBean
    private UpdateUserDetailsService updateUserDetailsService;

    @Test
    void shouldReturn201WhenWePostValidUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn415WhenWePostWithXmlContentType() throws Exception {
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_ATOM_XML))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturn405WhenInvalidMethodForPost() throws Exception {
        mockMvc.perform(delete(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isMethodNotAllowed());
    }

    @ParameterizedTest
    @ValueSource(strings = {"maikoku1", "maikoku!", "майкоев", "maiko ku", "Maikoku"})
    void shouldReturn400WhenAddedUsernameDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(test);
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername(test)).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(null);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAddedUsernameIsNull() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(null);
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername(null)).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAddedUsernameIsNotUnique() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(false);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenAddedUsernameLengthIs6Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("m".repeat(6));
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("m".repeat(6))).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenAddedUsernameLengthIsShorterThan6Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("m".repeat(5));
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("m".repeat(5))).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenAddedUsernameLengthIs100Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("m".repeat(100));
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("m".repeat(100))).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenAddedUsernameLengthIsLongerThan100Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("m".repeat(101));
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("m".repeat(101))).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "maiko11!k", "Maik o11!k", "MAIKO11!K", "Maiko11K", "Maiko!!K"})
    void shouldReturn400WhenAddedPasswordDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword(test);
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenAddedPasswordLengthIs8Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko1!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenAddedPasswordLengthIsShorterThan8Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko!2");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenAddedPasswordLengthIs20Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko!!!!!00000!!!!!");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenAddedPasswordLengthIsLongerThan20Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko!!!!!00000!!!!!0");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAddedEmailIsNull() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setFirstName("Екатерина");
        userDTO.setUsermail(null);

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail(null)).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAddedEmailIsNotUnique() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(false);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "maiko@", "maiko0gmail.com", "@gmail.com", "maiko@gmail"})
    void shouldReturn400WhenAddedEmailDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail(test);
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail(test)).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Екатерина", "Eкатерина1986", "Eкатерина!", "Katsiaryna",
            "Екатери на", "ЕКАТЕРАНА", "екатерина"
    })
    void shouldReturn400WhenAddedFirstNameDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName(test);

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.ACTIVE);

        when(validatorUsernameService.isUniqueUsername("m".repeat(6))).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn201WhenAddedFirstNameIs20Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Е".repeat(20));

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenAddedFirstNamegerThan20Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Е".repeat(21));

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WithMessageWhenPostNotUniqueUsername() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(false);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Пользователь существует. Укажите другой логин", errors.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Maikoku", "maikoku1", "maikoku!", "майкоев", "maiko ku"})
    void shouldReturn400WithMessageWhenUsernameDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(test);
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername(test)).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Логин не соотвествует требованиям. Пожалуйста, введите логин в соответствии " +
                        "с требованиями: от 6 до 100 букв латиницы в нижнем регистре", errors.get(0)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "maiko11!k", "Maik o11!k", "MAIKO11!K", "Maiko11K", "Maiko!!K"})
    void shouldReturn400WithMessageWhenPasswordDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword(test);
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Пароль не соотвествует требованиям. Пожалуйста, введите пароль в соответствии с " +
                        "требованиями: от 8 символов до 20 символов, состоящих из цифр, спец символов и букв " +
                        "в различном регистре", errors.get(0)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Mmmmm!1", "MMMMMmmmmm!!!!!22222n}"})
    void shouldReturn400WithMessageWhenPasswordLengthDoesNotMatchRequirements(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword(test);
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Пароль не соотвествует требованиям. Пожалуйста, введите пароль в соответствии с " +
                        "требованиями: от 8 символов до 20 символов, состоящих из цифр, спец символов и букв " +
                        "в различном регистре", errors.get(0)
        );
    }

    @Test
    void shouldReturn400WithMessageWhenUserMailIsNotUnique() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(false);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Пользователь с данной электронной почтой уже существует. " +
                        "Укажите другую электронную почту", errors.get(0)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"maiko@", "maiko0gmail.com", "@gmail.com", "майко@gmail.com", "maiko@gmail"})
    void shouldReturn400WithMessageWhenUserMailDoesNotMatchPattern(String test) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail(test);
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail(test)).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Электронная почта не соотвествует требованиям. Пожалуйста, введите электронную почту в " +
                        "соответствии с форматом: allen@example.com (до 100 символов)", errors.get(0)
        );
    }

    @Test
    void shouldReturn400WithMessageWhenUserMailLengthIsLongerThan100Elements() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("m".repeat(91).concat("@gmail.com"));
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("m".repeat(91).concat("@gmail.com"))).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        List<String> errors = (List<String>) map.get(ERRORS_KEY);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals(
                "Электронная почта не соотвествует требованиям. Пожалуйста, введите электронную почту " +
                        "до 100 символов", errors.get(0)
        );
    }

    @Test
    void shouldReturn400WithMessageWhenWePostUInvalidUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Map<String, Object> map = objectMapper.readValue(actualResponseBody, Map.class);
        Assertions.assertEquals(Map.of(MESSAGE_KEY, "Новый пользователь не создан"), map);
    }

    @Test
    void shouldReturnUserWhenWePostValidInput() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("maikoku");
        userDTO.setPassword("Maiko11!K");
        userDTO.setUsermail("maiko@gmail.com");
        userDTO.setFirstName("Екатерина");

        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(1L);
        addedUserDTO.setStatus(StatusEnumDTO.valueOf("ACTIVE"));

        when(validatorUsernameService.isUniqueUsername("maikoku")).thenReturn(true);
        when(validatorUserMailService.isUniqueUserMail("maiko@gmail.com")).thenReturn(true);
        when(addUserService.add(userDTO)).thenReturn(addedUserDTO);
        MvcResult mvcResult = mockMvc.perform(post(MAPPING_VERSION + AUTH_SIGNIN_URI)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Map.of(
                "Создан новый пользователь", addedUserDTO
        )));
    }
}