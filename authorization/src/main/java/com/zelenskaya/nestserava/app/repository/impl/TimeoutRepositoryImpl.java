package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.TimeoutRepository;
import com.zelenskaya.nestserava.app.repository.model.Timeout;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Optional;

@Repository
public class TimeoutRepositoryImpl extends GenericRepositoryImpl<Long, Timeout> implements TimeoutRepository {
    @Override
    public Optional<Timeout> findByPeriod(Integer period) {
        String hql = "select t from Timeout as t where t.period=:period";
        Query query = entityManager.createQuery(hql);
        query.setParameter("period", period);
        Timeout singleResult = (Timeout) query.getSingleResult();
        return Optional.of(singleResult);
    }
}
