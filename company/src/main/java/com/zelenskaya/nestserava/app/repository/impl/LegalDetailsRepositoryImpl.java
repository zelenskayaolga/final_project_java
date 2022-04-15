package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.LegalDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.LegalDetails;
import org.springframework.stereotype.Repository;

@Repository
public class LegalDetailsRepositoryImpl extends GenerateRepositoryImpl<Long, LegalDetails> implements LegalDetailsRepository {
}
