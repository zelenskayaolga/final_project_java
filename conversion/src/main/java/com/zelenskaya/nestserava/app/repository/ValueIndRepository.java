package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.ValueInd;
import com.zelenskaya.nestserava.app.repository.model.ValueIndEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValueIndRepository extends JpaRepository<ValueInd, Long> {
    Optional<ValueInd> findByValueInd(ValueIndEnum valueInd);
}
