package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.GetLegalsByNameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateLegalIdServiceImplTest {

    @InjectMocks
    private GenerateLegalIdServiceImpl generateLegalIdService;

    @Mock
    private GetLegalsByNameService getLegalsByNameService;

    @Test
    void shouldReturnLegalIdByNameWhenResponseEntityIsValid() {
        String nameLegal = "Company";
        String legalId = "LegalId";

        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body(Map.of(
                nameLegal, "Company", legalId, 1L
        ));

        when(getLegalsByNameService.getLegalByName(nameLegal)).thenReturn(responseEntity);
        Long id = generateLegalIdService.getId(nameLegal);
        Assertions.assertNotNull(id);
    }
}
