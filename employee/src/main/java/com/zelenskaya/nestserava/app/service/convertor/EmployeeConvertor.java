package com.zelenskaya.nestserava.app.service.convertor;

import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;

public interface EmployeeConvertor {
    AddEmployeeDTO convertToDTO(Employee employee);
}