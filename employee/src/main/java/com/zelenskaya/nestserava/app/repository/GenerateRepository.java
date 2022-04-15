package com.zelenskaya.nestserava.app.repository;

import java.util.List;

public interface GenerateRepository<I, T> {
    void add(T entity);

    T update(T entity);

    T findById(I id);

    List<T> findAll();
}
