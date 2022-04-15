package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.GenerateLegalIdService;
import com.zelenskaya.nestserava.app.service.GetLegalsByNameService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Map;

@Service
@AllArgsConstructor
public class GenerateLegalIdServiceImpl implements GenerateLegalIdService {
    private final GetLegalsByNameService getLegalsByNameService;

    @Override
    public Long getId(String nameLegal) {
        try {
            ResponseEntity<Object> responseEntity = getLegalsByNameService.getLegalByName(nameLegal);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
                assert body != null;
                return Long.parseLong(body.get("LegalId").toString());
            } else {
                throw new NoResultException("Компания по заданным параметрам не найдена");
            }
        } catch (FeignException e) {
            throw new NoResultException("Компания по заданным параметрам не найдена");
        }
    }
}

