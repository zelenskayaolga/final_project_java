package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.SearchLegal;
import com.zelenskaya.nestserava.app.service.SearchLegalService;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchLegalServiceImpl implements SearchLegalService {
    private final ServiceLegalConstants constants;
    private final LegalRepository legalRepository;
    private final LegalConvertor legalConvertor;

    @Override
    @Transactional
    public List<LegalDTO> get(SearchDTO searchDTO, PaginationDTO paginationDTO) throws ServiceLegalException {
        Page<Legal> legals;
        SearchLegal searchLegal = convert(searchDTO);
        try {
            switch (paginationDTO.getPagination()) {
                case DEFAULT: {
                    legals = getLegals(searchLegal, paginationDTO.getPage(), constants.getDefaultPage());
                    break;
                }
                case CUSTOMIZED: {
                    Integer customizedPage = paginationDTO.getCustomizedPage();
                    List<Integer> defaultPages = Arrays.asList(constants.getDefaultPages());
                    if (defaultPages.contains(customizedPage)) {
                        legals = getLegals(searchLegal, paginationDTO.getPage(), customizedPage);
                    } else {
                        throw new ServiceLegalException();
                    }
                    break;
                }
                default: {
                    legals = legalRepository.getAll(null, searchLegal);
                }
            }
        } catch (NullPointerException e) {
            legals = legalRepository.getAll(null, searchLegal);
        }
        if (!legals.isEmpty()) {
            return legals.stream()
                    .map(legalConvertor::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Page<Legal> getLegals(SearchLegal searchLegal, Integer page, Integer customizedPage) {
        Pageable pageable = PageRequest.of(page, customizedPage);
        return legalRepository.getAll(pageable, searchLegal);
    }

    private SearchLegal convert(SearchDTO searchDTO) {
        SearchLegal legalSearch = new SearchLegal();
        String legalName = searchDTO.getLegalName();
        legalSearch.setName(legalName);
        String unp = searchDTO.getUnp();
        legalSearch.setUnp(unp);
        String ibanByByn = searchDTO.getIbanByByn();
        legalSearch.setIbanByByn(ibanByByn);
        return legalSearch;
    }
}