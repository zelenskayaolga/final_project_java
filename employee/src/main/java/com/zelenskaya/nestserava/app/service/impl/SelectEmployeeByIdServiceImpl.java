package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.service.SelectEmployeeByIdService;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
@AllArgsConstructor
public class SelectEmployeeByIdServiceImpl implements SelectEmployeeByIdService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeConvertor employeeConvertor;

    @Override
    public AddEmployeeDTO getById(Long employeeId) {
        Employee employeeById = employeeRepository.findById(employeeId);
        if (employeeById != null) {
            return employeeConvertor.convertToDTO(employeeById);
        } else {
            throw new NoResultException("Сотрудника не существует");
        }
    }
}
