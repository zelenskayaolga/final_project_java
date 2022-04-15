package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.PositionLegalRepository;
import com.zelenskaya.nestserava.app.repository.model.PositionEnum;
import com.zelenskaya.nestserava.app.repository.model.PositionLegal;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Optional;

@Repository
public class PositionLegalRepositoryImpl extends GenerateRepositoryImpl<Long, PositionLegal> implements PositionLegalRepository {
    @Override
    public Optional<PositionLegal> findByPosition(PositionEnum position) {
        String hql = "select p from PositionLegal as p where p.position=:position";
        Query query = entityManager.createQuery(hql);
        query.setParameter("position", position);
        PositionLegal singleResult = (PositionLegal) query.getSingleResult();
        return Optional.of(singleResult);
    }
}
