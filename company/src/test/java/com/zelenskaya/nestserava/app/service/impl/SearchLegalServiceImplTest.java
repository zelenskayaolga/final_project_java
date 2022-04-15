package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.SearchLegal;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationEnumDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static com.zelenskaya.nestserava.app.service.impl.LegalServiceTestConstants.RIGHT_IBAN_LEGAL;
import static com.zelenskaya.nestserava.app.service.impl.LegalServiceTestConstants.RIGHT_UNP_LEGAL;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchLegalServiceImplTest {

    @InjectMocks
    private SearchLegalServiceImpl searchLegalService;

    @Mock
    private LegalRepository legalRepository;
    @Mock
    private ServiceLegalConstants constants;
    @Mock
    private LegalConvertor legalConvertor;

    @Test
    void shouldReturnLegalDTOWhenValidDefaultPagination() {
        Pageable pageable = PageRequest.of(1, 10);

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(PaginationEnumDTO.DEFAULT);
        paginationDTO.setPage(1);

        SearchLegal searchLegal = new SearchLegal();
        searchLegal.setName("Com");
        searchLegal.setUnp(String.valueOf(RIGHT_UNP_LEGAL));
        searchLegal.setIbanByByn(RIGHT_IBAN_LEGAL);

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setLegalName("Com");
        searchDTO.setUnp(String.valueOf(RIGHT_UNP_LEGAL));
        searchDTO.setIbanByByn(RIGHT_IBAN_LEGAL);

        Page<Legal> legals = new PageImpl<>(Collections.emptyList());
        when(constants.getDefaultPage()).thenReturn(10);
        when(legalRepository.getAll(pageable, searchLegal)).thenReturn(legals);
        List<LegalDTO> legalDTO = searchLegalService.get(searchDTO, paginationDTO);
        Assertions.assertEquals(Collections.emptyList(), legalDTO);
    }
}