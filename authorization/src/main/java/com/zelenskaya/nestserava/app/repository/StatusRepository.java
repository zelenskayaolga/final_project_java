package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;

import java.util.Optional;

public interface StatusRepository extends GenericRepository<Long, Status> {
    Optional<Status> findByStatus(StatusEnum statusEnum);
}
