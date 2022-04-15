package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.SessionRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionRepositoryImpl extends GenericRepositoryImpl<Long, Session> implements SessionRepository {
    @Override
    public Optional<Session> findByJwt(String jwt) {
        try {
            String hql = "select s from Session as s where s.jwtToken=:jwt";
            Query query = entityManager.createQuery(hql);
            query.setParameter("jwt", jwt);
            Session singleResult = (Session) query.getSingleResult();
            return Optional.of(singleResult);
        } catch (NoResultException | NonUniqueResultException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<String> getJwtByUserId(Long userId) {
        String hql = "select s.jwtToken from User as u join Session as s on u.id =:userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Session> getByUserId(Long userId) {
        String hql = "select s from Session as s WHERE s.user =(select u from User as u where u.id=:userId)";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
