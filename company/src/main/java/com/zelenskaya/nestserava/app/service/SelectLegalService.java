package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;

public interface SelectLegalService {
    LegalDTO getById(Long id);

    LegalDTO getByName(String nameLegal) throws ServiceLegalException;
}
