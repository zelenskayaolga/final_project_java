package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.controller.security.filter.ParseJwt;
import com.zelenskaya.nestserava.app.controller.security.util.JwtUtilsConversion;
import com.zelenskaya.nestserava.app.repository.ApplicationDetailsRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationDetails;
import com.zelenskaya.nestserava.app.service.GetUserByIdService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.UpdateApplicationDetailsService;
import com.zelenskaya.nestserava.app.service.config.ServiceConfig;
import com.zelenskaya.nestserava.app.service.model.UsernameDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UpdateApplicationDetailsServiceImpl implements UpdateApplicationDetailsService {
    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final GetUserByIdService getUserByIdService;
    private final LocalDateService localDateService;
    private final ServiceConfig serviceConfig;
    private final JwtUtilsConversion jwtUtils;
    private final ParseJwt parseJwt;

    @Override
    @Transactional
    public UsernameDTO updateDate(Long id, HttpServletRequest request) {
        Optional<ApplicationDetails> optionalApplicationDetails = applicationDetailsRepository.findById(id);
        if (optionalApplicationDetails.isPresent()) {
            ApplicationDetails applicationDetails = optionalApplicationDetails.get();
            LocalDate updateDate = localDateService.dateTimeWithZone(serviceConfig.getZoneTime());
            applicationDetails.setUpdateDate(updateDate);
            Long userId = generateUserId(request);
            applicationDetails.setUserId(userId);
            ApplicationDetails updatedApplicationDetails = applicationDetailsRepository.
                    save(applicationDetails);
            return convertToDTO(updatedApplicationDetails);
        }
        return null;
    }

    private Long generateUserId(HttpServletRequest request) {
        String jwt = parseJwt.parse(request);
        return jwtUtils.getIdFromJwtToken(jwt);
    }

    private UsernameDTO convertToDTO(ApplicationDetails updatedApplicationDetails) {
        UsernameDTO usernameDTO = new UsernameDTO();
        String username = generateUsername(updatedApplicationDetails);
        usernameDTO.setUsername(username);
        return usernameDTO;
    }

    private String generateUsername(ApplicationDetails updatedApplicationDetails) {
        Long userId = updatedApplicationDetails.getUserId();
        ResponseEntity<Object> responseEntity = getUserByIdService.getUserById(userId);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
            String string = body.get("username").toString();
            log.info("Username " + string);
            return string;
        }
        throw new NoResultException("Пользователь стаким id не найден");
    }
}
