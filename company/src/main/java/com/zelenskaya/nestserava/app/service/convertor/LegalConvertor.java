package com.zelenskaya.nestserava.app.service.convertor;

import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;

public interface LegalConvertor {
    LegalDTO convertToDTO(Legal legal);
}
