package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.exceptions.ServiceLegalException;
import com.zelenskaya.nestserava.app.service.model.LegalDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;

import java.util.List;

public interface SearchLegalService {
    List<LegalDTO> get(SearchDTO searchDTO, PaginationDTO paginationDTO) throws ServiceLegalException;
}