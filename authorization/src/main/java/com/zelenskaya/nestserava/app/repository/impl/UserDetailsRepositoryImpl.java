package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.UserDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepositoryImpl extends GenericRepositoryImpl<Long, UserDetails> implements UserDetailsRepository {
}
