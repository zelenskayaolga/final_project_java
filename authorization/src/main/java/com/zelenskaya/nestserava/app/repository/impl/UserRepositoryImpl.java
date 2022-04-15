package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.UserRepository;
import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    @Override
    public Optional<User> getByUsername(String login) {
        String hql = "select u from User as u where u.login=:login";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("login", login);
        try {
            User user = (User) query.getSingleResult();
            User result = Optional.of(user).orElse(null);
            return Optional.of(result);
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isUsername(String login) {
        String hql = "select u from User as u where u.login=:login";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("login", login);
        List resultList = query.getResultList();
        return resultList.isEmpty();
    }

    @Override
    public Optional<User> getByUsermail(String eMail) {
        String hql = "select u from User as u where u.eMail=:eMail";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("eMail", eMail);
        try {
            User user = (User) query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isUsermail(String usermail) {
        String hql = "select u from User as u where u.eMail=:usermail";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("usermail", usermail);
        List resultList = query.getResultList();
        return resultList.isEmpty();
    }

    @Override
    public User getByLogin(String login) {
        String hql = "select u from User as u where u.eMail=:eMail";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("eMail", login);
        return (User) query.getParameters();
    }

    @Override
    public User getByEmail(String login) {
        String hql = "select u from User as u where u.eMail=:eMail";
        Query query = entityManager.createQuery(hql, User.class);
        query.setParameter("eMail", login);
        return (User) query.getSingleResult();
    }

    @Override
    public List<Session> getByUserId(Long userId) {
        String hql = "select u.sessions from User as u WHERE u.id=:userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
