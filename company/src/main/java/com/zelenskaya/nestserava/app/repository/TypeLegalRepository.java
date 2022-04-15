package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.TypeEnum;
import com.zelenskaya.nestserava.app.repository.model.TypeLegal;

import java.util.Optional;

public interface TypeLegalRepository extends GenerateRepository<Long, TypeLegal> {
    Optional<TypeLegal> findByType(TypeEnum type);
}
