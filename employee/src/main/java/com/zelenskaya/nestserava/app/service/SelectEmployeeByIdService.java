package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;

public interface SelectEmployeeService {
    AddEmployeeDTO getById(Long employeeId);
}
