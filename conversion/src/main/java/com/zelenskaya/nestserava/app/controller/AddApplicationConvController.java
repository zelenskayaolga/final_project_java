package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.CvsAddService;
import com.zelenskaya.nestserava.app.service.model.AddedApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.util.CvsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "${mapping.version}/files",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AddApplicationConvController {
    private static final String MESSAGE_KEY = "message";
    private final CvsUtil cvsUtil;
    private final CvsAddService cvsService;

    @PostMapping
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("UUID " + UUID.randomUUID());
        try {
            if (cvsUtil.isCSVFormat(file)) {
                List<AddedApplicationConvDTO> applicationsConv = cvsService.addApplicationConv(file);
                return ResponseEntity.ok().
                        body(Map.of(MESSAGE_KEY, "Сохранено " + applicationsConv.size() + " заявок"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body(Map.of(MESSAGE_KEY, "Недопустимый формат файла"));
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Список не соответствует параметрам");
        }
    }
}