package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Optional;

@Repository
public class StatusRepositoryImpl extends GenericRepositoryImpl<Long, Status> implements StatusRepository {
    @Override
    public Optional<Status> findByStatus(StatusEnum statusEnum) {
        String hql = "select s from Status as s where s.status=:status";
        Query query = entityManager.createQuery(hql);
        query.setParameter("status", statusEnum);
        Status singleResult = (Status) query.getSingleResult();
        return Optional.of(singleResult);
    }
}
