package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.RoleRepository;
import com.zelenskaya.nestserava.app.repository.model.Role;
import com.zelenskaya.nestserava.app.repository.model.RoleEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
    @Override
    public Optional<Role> findByRole(RoleEnum role) {
        String hql = "select r from Role as r where r.role=:role";
        Query query = entityManager.createQuery(hql);
        query.setParameter("role", role);
        Role singleResult = (Role) query.getSingleResult();
        return Optional.of(singleResult);
    }
}
