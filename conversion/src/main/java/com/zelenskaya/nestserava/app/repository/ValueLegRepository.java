package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.ValueLeg;
import com.zelenskaya.nestserava.app.repository.model.ValueLegEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValueLegRepository extends JpaRepository<ValueLeg, Long> {
    Optional<ValueLeg> findByValueLeg(ValueLegEnum valueLegEnum);
}
