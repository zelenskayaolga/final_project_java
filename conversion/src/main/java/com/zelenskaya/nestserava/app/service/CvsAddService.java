package com.zelenskaya.nestserava.app.service;

import com.zelenskaya.nestserava.app.service.model.AddedApplicationConvDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CvsAddService {
    List<AddedApplicationConvDTO> addApplicationConv(MultipartFile file);
}
