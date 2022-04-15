package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.LogoutDTO;

public interface CloseSessionService {
    boolean close(LogoutDTO logoutDTO);
}
