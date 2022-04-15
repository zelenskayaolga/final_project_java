package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationConvRepository extends JpaRepository<ApplicationConv, Long>,
        PagingAndSortingRepository<ApplicationConv, Long> {
    Optional<ApplicationConv> findApplicationConvByApplicationConvId(String applicationConvId);

    boolean existsApplicationConvByIdAndApplicationConvId(Long id, String applicationConvId);
}