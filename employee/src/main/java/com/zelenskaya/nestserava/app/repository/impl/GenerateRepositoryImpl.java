package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.GenerateRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GenerateRepositoryImpl<I, T> implements GenerateRepository<I, T> {
    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public GenerateRepositoryImpl() {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperClass.getActualTypeArguments()[1];
    }

    @Override
    public void add(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        String queryString = "from " + entityClass.getSimpleName();
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
}
