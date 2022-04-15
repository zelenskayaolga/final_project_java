package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.UserDTO;

public interface BlockUserService {
    UserDTO blockByUsername(String username);
}
