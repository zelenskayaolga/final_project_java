package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.TypeLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.TypeEnum;
import com.zelenskaya.nestserava.app.repository.model.TypeLegal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@Slf4j
public class TypeLegalRepositoryImpl extends GenerateRepositoryImpl<Long, TypeLegal> implements TypeLegalRepository {
    @Override
    public Optional<TypeLegal> findByType(TypeEnum type) {
        String hql = "select r from TypeLegal as r where r.type=:type";
        Query query = entityManager.createQuery(hql);
        query.setParameter("type", type);
        TypeLegal singleResult = (TypeLegal) query.getSingleResult();
        try {
            return Optional.of(singleResult);
        } catch (NoResultException e) {
            log.error("No companies were found for the specified parameter", e);
        } catch (NonUniqueResultException e) {
            log.error("Several companies were found by the specified parameter", e);
        }
        return Optional.ofNullable(singleResult);
    }
}