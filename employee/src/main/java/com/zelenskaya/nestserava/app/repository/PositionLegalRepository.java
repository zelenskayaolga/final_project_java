package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.PositionEnum;
import com.zelenskaya.nestserava.app.repository.model.PositionLegal;

import java.util.Optional;

public interface PositionLegalRepository extends GenerateRepository<Long, PositionLegal> {
    Optional<PositionLegal> findByPosition(PositionEnum position);
}
