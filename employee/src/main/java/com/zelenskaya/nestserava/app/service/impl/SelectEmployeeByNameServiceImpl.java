package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.service.SelectEmployeeByNameService;
import com.zelenskaya.nestserava.app.service.convertor.EmployeeConvertor;
import com.zelenskaya.nestserava.app.service.model.AddEmployeeDTO;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SelectEmployeeByNameServiceImpl implements SelectEmployeeByNameService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeConvertor employeeConvertor;

    @Override
    @Transactional
    public List<AddEmployeeDTO> getByFullNameIndividual(String name) {
        List<AddEmployeeDTO> employeesDTO = new ArrayList<>();
        List<Employee> employees = employeeRepository.getByFullName(name);
        if (!employees.isEmpty()) {
            for (Employee employee : employees) {
                AddEmployeeDTO employeeDTO = employeeConvertor.convertToDTO(employee);
                employeesDTO.add(employeeDTO);
            }
            return employeesDTO;
        }
        throw new ServiceException("Сотрудник компании c указанным именем не найден");
    }
}
