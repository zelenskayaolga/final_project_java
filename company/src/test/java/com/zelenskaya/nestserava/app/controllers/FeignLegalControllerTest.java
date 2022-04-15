package com.zelenskaya.nestserava.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zelenskaya.nestserava.app.config.JwtUtilsConfig;
import com.zelenskaya.nestserava.app.controllers.security.point.AuthEntryPointJwtCompany;
import com.zelenskaya.nestserava.app.controllers.security.util.JwtUtilsCompany;
import com.zelenskaya.nestserava.app.service.PostJwtService;
import com.zelenskaya.nestserava.app.service.SelectLegalService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.zelenskaya.nestserava.app.controllers.LegalControllerTestConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FeignLegalController.class)
class FeignLegalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SelectLegalService selectLegalService;
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

    @Test
    @WithMockUser
    void shouldReturn200WhenWeGetLegalDTOById() throws Exception {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_LEGAL_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(selectLegalService.getByName(RIGHT_NAME_LEGAL)).thenReturn(legalDTO);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_FEIGN_LEGAL_CONTROLLER)
                        .param("Name_Legal", RIGHT_NAME_LEGAL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBadRequest() throws Exception {
        when(selectLegalService.getByName("Company")).thenThrow(ServiceLegalException.class);
        mockMvc.perform(get(RIGHT_URL_TEMPLATE_FOR_FEIGN_LEGAL_CONTROLLER)
                        .param("Name_Legal", "Company")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}