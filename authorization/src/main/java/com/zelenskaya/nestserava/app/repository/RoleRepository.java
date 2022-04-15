package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Role;
import com.zelenskaya.nestserava.app.repository.model.RoleEnum;

import java.util.Optional;

public interface RoleRepository extends GenericRepository<Long, Role> {
    Optional<Role> findByRole(RoleEnum role);
}
