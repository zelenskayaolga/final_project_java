package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.zelenskaya.nestserava.app.service.impl.LegalServiceTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectLegalServiceImplTest {

    @InjectMocks
    private SelectLegalServiceImpl selectLegalService;

    @Mock
    private LegalRepository legalRepository;
    @Mock
    private LegalConvertor legalConvertor;

    @Test
    void shouldReturnLegalDTOWhenWeGetItById() {
        Legal legal = new Legal();
        legal.setId(RIGHT_ID);
        legal.setNameLegal(RIGHT_NAME_LEGAL);
        legal.setUnp(String.valueOf(RIGHT_UNP_LEGAL));

        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(legalRepository.findById(legal.getId())).thenReturn(legal);
        when(legalConvertor.convertToDTO(legal)).thenReturn(legalDTO);
        LegalDTO legalDTObyId = selectLegalService.getById(RIGHT_ID);
        Assertions.assertEquals(legalDTO, legalDTObyId);
    }

    @Test
    void shouldReturnNullWhenLegalByIdDoesNotExist() {
        Legal legal = new Legal();
        legal.setId(null);
        legal.setNameLegal(RIGHT_NAME_LEGAL);
        legal.setUnp(String.valueOf(RIGHT_UNP_LEGAL));

        when(legalRepository.findById(legal.getId())).thenReturn(null);
        LegalDTO legalDTObyId = selectLegalService.getById(null);
        Assertions.assertEquals(null, legalDTObyId);
    }

    @Test
    void shouldReturnLegalDTOWhenWeGetItByName() {
        Legal legal = new Legal();
        legal.setId(RIGHT_ID);
        legal.setNameLegal(RIGHT_NAME_LEGAL);
        legal.setUnp(String.valueOf(RIGHT_UNP_LEGAL));

        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(legalRepository.findByNameLegal(legal.getNameLegal())).thenReturn(legal);
        when(legalConvertor.convertToDTO(legal)).thenReturn(legalDTO);
        LegalDTO legalDTObyName = selectLegalService.getByName(RIGHT_NAME_LEGAL);
        Assertions.assertEquals(legalDTO, legalDTObyName);
    }

    @Test
    void shouldReturnExceptionWhenLegalByNameDoesNotExist() {
        when(legalRepository.findByNameLegal(RIGHT_NAME_LEGAL)).thenReturn(null);
        ServiceLegalException exception = assertThrows(
                ServiceLegalException.class, () -> selectLegalService.getByName(RIGHT_NAME_LEGAL)
        );
        Assertions.assertEquals("Компания с указанным именем не существует", exception.getMessage());
    }
}
