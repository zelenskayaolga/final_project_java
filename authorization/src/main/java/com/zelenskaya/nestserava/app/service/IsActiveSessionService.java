package com.zelenskaya.nestserava.app.service;

public interface IsActiveSessionService {
    boolean isActive(String jwt);
}
