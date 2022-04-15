package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import com.zelenskaya.nestserava.app.service.model.AddedEmployeeDTO;

public interface AddEmployeeService {
    AddedEmployeeDTO add(AddEmployeeDTO addEmployeeDTO);
}
