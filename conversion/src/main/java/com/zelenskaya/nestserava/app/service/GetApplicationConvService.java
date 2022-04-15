package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;

import java.util.List;

public interface GetApplicationConvService {
    ApplicationConvDTO getById(Long id);

    List<ApplicationConvDTO> get(PaginationDTO paginationDTO);
}