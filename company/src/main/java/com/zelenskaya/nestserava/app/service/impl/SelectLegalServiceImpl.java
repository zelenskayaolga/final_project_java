package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.service.SelectLegalService;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SelectLegalServiceImpl implements SelectLegalService {
    private final LegalRepository legalRepository;
    private final LegalConvertor legalConvertor;

    @Override
    @Transactional
    public LegalDTO getById(Long id) {
        Legal legalById = legalRepository.findById(id);
        if (legalById != null) {
            return legalConvertor.convertToDTO(legalById);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public LegalDTO getByName(String nameLegal) throws ServiceLegalException {
        Legal legalByNameLegal = legalRepository.findByNameLegal(nameLegal);
        if (legalByNameLegal != null) {
            return legalConvertor.convertToDTO(legalByNameLegal);
        } else {
            throw new ServiceLegalException("Компания с указанным именем не существует");
        }
    }
}