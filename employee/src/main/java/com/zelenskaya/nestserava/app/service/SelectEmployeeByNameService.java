package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;

import java.util.List;

public interface SelectEmployeeByNameService {
    List<AddEmployeeDTO> getByFullNameIndividual(String name);
}
