package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.UpdateNameLegalDTO;

import javax.servlet.http.HttpServletRequest;

public interface UpdateNameLegalService {
    UpdateNameLegalDTO update(
            Long id,
            UpdateNameLegalDTO updateNameLegalDTO,
            HttpServletRequest request
    );
}
