package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;

public interface AddLegalService {
    LegalDTO add(LegalDTO legalDTO) throws ServiceLegalException;
}
