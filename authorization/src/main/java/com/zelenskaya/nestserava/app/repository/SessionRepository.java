package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends GenericRepository<Long, Session> {
    Optional<Session> findByJwt(String jwt);

    List<String> getJwtByUserId(Long id);

    List<Session> getByUserId(Long userId);
}
