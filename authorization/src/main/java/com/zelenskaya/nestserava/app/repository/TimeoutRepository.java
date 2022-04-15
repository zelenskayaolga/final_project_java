package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Timeout;

import java.util.Optional;

public interface TimeoutRepository extends GenericRepository<Long, Timeout> {
    Optional<Timeout> findByPeriod(Integer period);
}
