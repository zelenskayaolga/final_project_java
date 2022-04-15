package com.zelenskaya.nestserava.app.repository.impl;

import com.zelenskaya.nestserava.app.repository.EmployeeRepository;
import com.zelenskaya.nestserava.app.repository.model.Employee;
import com.zelenskaya.nestserava.app.repository.model.SearchEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl extends GenerateRepositoryImpl<Long, Employee> implements EmployeeRepository {
    @Override
    public List<Employee> getByFullName(String name) {
        String hql = "select e from Employee as e where e.name=:name";
        Query query = entityManager.createQuery(hql);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public Page<Employee> getAll(Pageable pageable, SearchEmployee searchEmployee) {
        List<Long> legalIds = searchEmployee.getLegalId();
        String fullNameIndividual = searchEmployee.getFullNameIndividual();

        List queryResultList;
        if (legalIds == null) {
            queryResultList = getQueryResultList(pageable, fullNameIndividual);
        } else {
            queryResultList = getQueryResultList(pageable, legalIds, fullNameIndividual);
        }
        List<Employee> employees = new ArrayList(queryResultList);
        return new PageImpl<>(employees);
    }

    private List<Employee> getQueryResultList(Pageable pageable, List<Long> legalIds, String fullNameIndividual) {
        List employees = new ArrayList<>();
        int countOfEmployees = 0;
        for (Long legalId : legalIds) {
            Query query = getQueryFromCondition(fullNameIndividual, legalId);
            List queryResultList = query.getResultList();
            if (pageable == null) {
                employees.addAll(queryResultList);
            } else {
                for (Object queryResult : queryResultList) {
                    if (isAccordingToPageCondition(pageable, countOfEmployees)) {
                        if (isPageNotFull(pageable, employees)) {
                            Employee employee = (Employee) queryResult;
                            employees.add(employee);
                        } else {
                            break;
                        }
                    }
                    countOfEmployees++;
                }
            }
        }
        return employees;
    }

    private boolean isPageNotFull(Pageable pageable, List employees) {
        return employees.size() != pageable.getPageSize();
    }

    private List getQueryResultList(Pageable pageable, String fullNameIndividual) {
        Query query = getQueryFromCondition(fullNameIndividual, null);
        if (pageable != null) {
            int pageSize = pageable.getPageSize();
            int pageNumber = pageable.getPageNumber();
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    private Query getQueryFromCondition(String fullNameIndividual, Long legalId) {
        StringBuilder command = new StringBuilder("SELECT e FROM Employee as e");
        boolean addedParameter = false;
        addedParameter = isAddParameter(command, legalId, addedParameter, " e.legalId = :legalId");
        isAddParameter(command, fullNameIndividual, addedParameter, " e.name LIKE :fullName");
        command.append(" ORDER BY e.id asc");
        Query query = entityManager.createQuery(command.toString());
        if (legalId != null) {
            query.setParameter("legalId", legalId);
        }
        if (fullNameIndividual != null) {
            query.setParameter("fullName", fullNameIndividual + '%');
        }
        return query;
    }

    private boolean isAccordingToPageCondition(Pageable pageable, int countOfEmployee) {
        return countOfEmployee >= (pageable.getPageNumber() - 1) * pageable.getPageSize()
                && countOfEmployee < pageable.getPageNumber() * pageable.getPageSize();
    }

    private boolean isAddParameter(StringBuilder command, Object field, boolean addedParameter, String addedCommand) {
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
}
