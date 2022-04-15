package com.zelenskaya.nestserava.app.service;

public interface ValidatorSessionUsernameService {
    boolean isValid(String username, String jwt);
}
