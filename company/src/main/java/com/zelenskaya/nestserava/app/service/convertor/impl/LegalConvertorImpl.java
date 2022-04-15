package com.zelenskaya.nestserava.app.service.convertor.impl;

import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.TypeEnumDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LegalConvertorImpl implements LegalConvertor {
    @Override
    @Transactional
    public LegalDTO convertToDTO(Legal legal) {
        LegalDTO legalDTO = new LegalDTO();
        legalDTO.setId(legal.getId());
        legalDTO.setNameLegal(legal.getNameLegal());
        legalDTO.setUnp(Integer.valueOf(legal.getUnp()));
        legalDTO.setIban(legal.getIbanByByn());
        String typeString = legal.getType().getType().name();
        legalDTO.setTypeLegal(TypeEnumDTO.valueOf(typeString));
        legalDTO.setTotalEmployees(legal.getTotalEmployee());
        return legalDTO;
    }
}
