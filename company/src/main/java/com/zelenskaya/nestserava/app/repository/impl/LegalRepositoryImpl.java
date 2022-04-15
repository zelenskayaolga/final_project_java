package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.LegalRepository;
import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.SearchLegal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class LegalRepositoryImpl extends GenerateRepositoryImpl<Long, Legal> implements LegalRepository {
    @Override
    public Page<Legal> getAll(Pageable pageable, SearchLegal searchLegal) {
        StringBuilder command = new StringBuilder("SELECT l FROM Legal as l");
        boolean addedParameter = false;

        String name = searchLegal.getName();
        addedParameter = isAddParameter(command, name, addedParameter, " l.nameLegal LIKE :name");
        String unp = searchLegal.getUnp();
        addedParameter = isAddParameter(command, unp, addedParameter, " l.unp LIKE :unp");
        String ibanByByn = searchLegal.getIbanByByn();
        isAddParameter(command, ibanByByn, addedParameter, " l.ibanByByn LIKE :iban");
        command.append(" ORDER BY l.id asc");
        Query query = entityManager.createQuery(command.toString());
        if (name != null) {
            query.setParameter("name", name + '%');
        }
        if (unp != null) {
            query.setParameter("unp", unp + '%');
        }
        if (ibanByByn != null) {
            query.setParameter("iban", ibanByByn + '%');
        }
        if (pageable != null) {
            int pageSize = pageable.getPageSize();
            int pageNumber = pageable.getPageNumber();
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        List resultList = query.getResultList();
        return new PageImpl<>(resultList);
    }

    private boolean isAddParameter(StringBuilder command, String field, boolean addedParameter, String addedCommand) {
        if (field != null) {
            if (addedParameter) {
                command.append(" AND");
            } else {
                command.append(" WHERE");
                addedParameter = true;
            }
            command.append(addedCommand);
        }
        return addedParameter;
    }

    @Override
    public Legal findByNameLegal(String nameLegal) {
        String hql = "select l from Legal as l where l.nameLegal=:nameLegal";
        Query query = entityManager.createQuery(hql);
        query.setParameter("nameLegal", nameLegal);
        try {
            return (Legal) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
}
