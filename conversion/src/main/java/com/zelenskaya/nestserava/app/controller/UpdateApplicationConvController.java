package com.zelenskaya.nestserava.app.controller;

import com.zelenskaya.nestserava.app.service.UpdateNameLegalService;
import com.zelenskaya.nestserava.app.service.UpdateStatusService;
import com.zelenskaya.nestserava.app.service.model.UpdateNameLegalDTO;
import com.zelenskaya.nestserava.app.service.model.UpdateStatusDTO;
import com.zelenskaya.nestserava.app.service.model.UpdatedStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(
        value = "${mapping.version}/applications",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class UpdateApplicationConvController {
    private static final String MESSAGE_KEY = "message";
    private final UpdateNameLegalService updateApplicationConvService;
    private final UpdateStatusService updateStatusService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateNameLegal(
            @PathVariable Long id,
            @RequestBody @Validated UpdateNameLegalDTO updateNameLegalDTO,
            HttpServletRequest request
    ) {
        String applicationConvId = updateNameLegalDTO.getApplicationConvId();
        String nameLegal = updateNameLegalDTO.getNameLegal();
        UpdateNameLegalDTO updatedApplicationConv = updateApplicationConvService.
                update(id, updateNameLegalDTO, request);
        if (updatedApplicationConv != null) {
            return ResponseEntity.ok().body(Map.of(MESSAGE_KEY,
                    "Заявка на конверсию " + applicationConvId +
                            " перепривязана к " + nameLegal));
        } else {
            return ResponseEntity.ok().body(Map.of(MESSAGE_KEY,
                    "Заявка на конверсию " + applicationConvId +
                            " привязана к " + nameLegal));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateStatus(
            @RequestBody @Validated UpdateStatusDTO updateStatusDTO,
            HttpServletRequest request
    ) {
        UpdatedStatusDTO updatedStatusDTO = updateStatusService.update(updateStatusDTO, request);
        if (updatedStatusDTO != null) {
            return ResponseEntity.ok()
                    .body(Map.of("Статус изменен", updatedStatusDTO));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(MESSAGE_KEY, "Сервер не доступен"));
        }
    }
}
