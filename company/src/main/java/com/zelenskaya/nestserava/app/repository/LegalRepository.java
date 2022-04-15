package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Legal;
import com.zelenskaya.nestserava.app.repository.model.SearchLegal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LegalRepository extends GenerateRepository<Long, Legal> {
    Page<Legal> getAll(Pageable pageable, SearchLegal searchLegal);

    Legal findByNameLegal(String nameLegal);
}
