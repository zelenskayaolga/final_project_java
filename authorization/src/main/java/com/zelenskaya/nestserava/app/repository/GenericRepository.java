package com.zelenskaya.nestserava.app.repository;

public interface GenericRepository<I, T> {
    void add(T entity);

    T update(T entity);

    T findById(I id);
}
