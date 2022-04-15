package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.UpdateStatusDTO;
import com.zelenskaya.nestserava.app.service.model.UpdatedStatusDTO;

import javax.servlet.http.HttpServletRequest;

public interface UpdateStatusService {
    UpdatedStatusDTO update(UpdateStatusDTO updateStatusDTO, HttpServletRequest request);
}
