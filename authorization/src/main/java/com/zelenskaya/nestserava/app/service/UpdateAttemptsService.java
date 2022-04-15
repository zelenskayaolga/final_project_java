package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AttemptsDTO;

public interface UpdateAttemptsService {
    AttemptsDTO updateByUsername(String username);

    void updateToZero(String username);
}
