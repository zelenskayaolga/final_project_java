package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.ApplicationConvRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import com.zelenskaya.nestserava.app.service.GenerateLegalIdService;
import com.zelenskaya.nestserava.app.service.UpdateApplicationDetailsService;
import com.zelenskaya.nestserava.app.service.model.UpdateNameLegalDTO;
import com.zelenskaya.nestserava.app.service.model.UsernameDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateNameLegalServiceImplTest {

    @InjectMocks
    private UpdateNameLegalServiceImpl updateNameLegalService;

    @Mock
    private ApplicationConvRepository applicationConvRepository;
    @Mock
    private GenerateLegalIdService generateLegalIdService;
    @Mock
    private UpdateApplicationDetailsService updateApplicationDetailsService;

    @Test
    void ShouldReturnUpdateNameLegalDTOWhenValidInput() {
        Long id = 1L;
        String nameLegal = "Company";
        String applicationConvId = "1aba1f74-4165-47e3-a54d-0179c6e098a4";

        HttpServletRequest request = mock(HttpServletRequest.class);

        ApplicationConv applicationConv = new ApplicationConv();
        applicationConv.setId(id);

        UsernameDTO usernameDTO = new UsernameDTO();
        usernameDTO.setUsername("userName");

        UpdateNameLegalDTO updateNameLegalDTO = new UpdateNameLegalDTO();
        updateNameLegalDTO.setNameLegal(nameLegal);
        updateNameLegalDTO.setApplicationConvId(applicationConvId);

        when(applicationConvRepository.existsApplicationConvByIdAndApplicationConvId(id, applicationConvId))
                .thenReturn(true);
        when(generateLegalIdService.getId(nameLegal)).thenReturn(1L);
        when(applicationConvRepository.findApplicationConvByApplicationConvId(applicationConvId))
                .thenReturn(Optional.of(applicationConv));
        when(applicationConvRepository.save(applicationConv)).thenReturn(applicationConv);
        when(updateApplicationDetailsService.updateDate(applicationConv.getId(), request)).thenReturn(usernameDTO);
        UpdateNameLegalDTO updateNameLegal = updateNameLegalService.update(id, updateNameLegalDTO, request);
        Assertions.assertNotNull(updateNameLegal);
    }
}
