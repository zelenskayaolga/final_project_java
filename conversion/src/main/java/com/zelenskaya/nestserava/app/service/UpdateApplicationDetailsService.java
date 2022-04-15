package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.UsernameDTO;

import javax.servlet.http.HttpServletRequest;

public interface UpdateApplicationDetailsService {
    UsernameDTO updateDate(Long id, HttpServletRequest request);
}
