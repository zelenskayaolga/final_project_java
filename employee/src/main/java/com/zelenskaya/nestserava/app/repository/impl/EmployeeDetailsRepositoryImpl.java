package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.EmployeeDetails;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDetailsRepositoryImpl extends GenerateRepositoryImpl<Long, EmployeeDetails> implements EmployeeDetailsRepository {
}
