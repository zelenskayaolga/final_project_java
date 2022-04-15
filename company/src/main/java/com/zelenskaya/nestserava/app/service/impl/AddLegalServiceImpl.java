package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.LegalDetailsRepository;
import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.TypeLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.LegalDetails;
import com.zelenskaya.nestserava.app.repository.model.TypeEnum;
import com.zelenskaya.nestserava.app.repository.model.TypeLegal;
import com.zelenskaya.nestserava.app.service.AddLegalService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.convertor.LegalConvertor;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddLegalServiceImpl implements AddLegalService {
    private final LegalRepository legalRepository;
    private final LegalDetailsRepository legalDetailsRepository;
    private final TypeLegalRepository typeLegalRepository;
    private final LocalDateService localDateService;
    private final LegalConvertor legalConvertor;

    @Value("${time.zone}")
    private String zoneTime;

    @Override
    @Transactional
    public LegalDTO add(LegalDTO legalDTO) throws ServiceLegalException {
        Legal legal = convertToLegal(legalDTO);
        String typeString = legalDTO.getTypeLegal().name();
        Optional<TypeLegal> type = typeLegalRepository.findByType(TypeEnum.valueOf(typeString));
        type.ifPresent(legal::setType);
        LocalDate localDate = localDateService.dateTimeWithZone(zoneTime);
        LegalDetails legalDetails = generateLocalDetails(localDate);
        legal.setLegalDetails(legalDetails);
        legalDetails.setLegal(legal);
        try {
            legalRepository.add(legal);
            legalDetailsRepository.add(legalDetails);
            return legalConvertor.convertToDTO(legal);
        } catch (EntityExistsException | DataIntegrityViolationException e) {
            throw new ServiceLegalException();
        }
    }

    private LegalDetails generateLocalDetails(LocalDate localDate) {
        LegalDetails legalDetails = new LegalDetails();
        legalDetails.setCreationDate(localDate);
        return legalDetails;
    }

    private Legal convertToLegal(LegalDTO legalDTO) {
        Legal legal = new Legal();
        legal.setNameLegal(legalDTO.getNameLegal());
        legal.setUnp(String.valueOf(legalDTO.getUnp()));
        legal.setIbanByByn(legalDTO.getIban());
        String stringEnum = legalDTO.getTypeLegal().name();
        TypeLegal typeLegal = new TypeLegal();
        typeLegal.setType(TypeEnum.valueOf(stringEnum));
        legal.setType(typeLegal);
        legal.setTotalEmployee(legalDTO.getTotalEmployees());
        return legal;
    }
}
