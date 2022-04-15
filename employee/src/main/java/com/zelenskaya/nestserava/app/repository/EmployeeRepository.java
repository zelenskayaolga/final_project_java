package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.SearchEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeRepository extends GenerateRepository<Long, Employee> {
    Page<Employee> getAll(Pageable pageable, SearchEmployee searchEmployee);

    List<Employee> getByFullName(String name);
}
