package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.AttemptsRepository;
import com.zelenskaya.nestserava.app.repository.model.Attempts;
import org.springframework.stereotype.Repository;

@Repository
public class AttemptsRepositoryImpl extends GenericRepositoryImpl<Long, Attempts> implements AttemptsRepository {
}
