package com.zelenskaya.nestserava.app.repository;

import com.zelenskaya.nestserava.app.repository.model.Session;
import com.zelenskaya.nestserava.app.repository.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends GenericRepository<Long, User> {
    Optional<User> getByUsername(String login);

    boolean isUsername(String username);

    Optional<User> getByUsermail(String eMail);

    boolean isUsermail(String usermail);

    List<Session> getByUserId(Long userId);

    User getByLogin(String login);

    User getByEmail(String login);
}
