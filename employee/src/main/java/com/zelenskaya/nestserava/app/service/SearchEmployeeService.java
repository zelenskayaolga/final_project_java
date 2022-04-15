package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.SearchDTO;

import java.util.List;

public interface SearchEmployeeService {
    List<AddEmployeeDTO> get(SearchDTO searchDTO, PaginationDTO paginationDTO);
}