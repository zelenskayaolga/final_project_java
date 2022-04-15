package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.ApplicationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDetailsRepository extends JpaRepository<ApplicationDetails, Long> {
}
