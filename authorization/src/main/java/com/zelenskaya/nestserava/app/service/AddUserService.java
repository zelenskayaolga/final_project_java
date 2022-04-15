package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddedUserDTO;
import com.zelenskaya.nestserava.app.service.model.UserDTO;

public interface AddUserService {
    AddedUserDTO add(UserDTO userDTO);
}
