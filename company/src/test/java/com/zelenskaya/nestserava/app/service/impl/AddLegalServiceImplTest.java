package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalDetailsRepository;
import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.TypeLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.TypeEnum;
import com.zelenskaya.nestserava.app.repository.model.TypeLegal;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Optional;

import static com.zelenskaya.nestserava.app.service.impl.LegalServiceTestConstants.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddLegalServiceImplTest {

    @InjectMocks
    private AddLegalServiceImpl addLegalService;

    @Mock
    private LegalRepository legalRepository;
    @Mock
    private LegalDetailsRepository legalDetailsRepository;
    @Mock
    private TypeLegalRepository typeLegalRepository;
    @Mock
    private LocalDateService localDateService;
    @Mock
    private LegalConvertor legalConvertor;

    @Value("${time.zone}")
    private String zoneTime;

    @Test
    void shouldReturnLegalDTOWhenValidInput() {
        TypeLegal typeLegal = new TypeLegal();
        typeLegal.setId(RIGHT_ID);
        typeLegal.setType(TypeEnum.RESIDENT);

        Legal legal = new Legal();
        legal.setNameLegal(RIGHT_NAME_LEGAL);
        legal.setUnp(String.valueOf(RIGHT_UNP_LEGAL));
        legal.setIbanByByn(RIGHT_IBAN_LEGAL);
        legal.setType(typeLegal);
        legal.setTotalEmployee(RIGHT_TOTAL_EMPLOYEES);

        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(RIGHT_ID);
        legalDTO.setNameLegal(RIGHT_NAME_LEGAL);
        legalDTO.setUnp(RIGHT_UNP_LEGAL);
        legalDTO.setIban(RIGHT_IBAN_LEGAL);
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(RIGHT_TYPE_LEGAL));
        legalDTO.setTotalEmployees(RIGHT_TOTAL_EMPLOYEES);

        when(typeLegalRepository.findByType(typeLegal.getType())).thenReturn(Optional.of(typeLegal));
        when(localDateService.dateTimeWithZone(zoneTime)).thenReturn(LocalDate.now());
        when(legalConvertor.convertToDTO(legal)).thenReturn(legalDTO);
        LegalDTO addedLegalDTO = addLegalService.add(legalDTO);
        Assertions.assertEquals(legalDTO, addedLegalDTO);
    }
}