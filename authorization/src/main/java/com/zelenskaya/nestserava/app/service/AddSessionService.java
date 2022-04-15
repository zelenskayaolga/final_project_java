package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AuthDTO;

public interface AddSessionService {
    AuthDTO addWithUsername(String jwtToken, String username);
}
