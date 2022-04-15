package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.UserDTO;

public interface SelectUserByUsernameService {
    UserDTO findByUsername(String username);
}
