package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.ApplicationConvRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import com.zelenskaya.nestserava.app.service.GenerateLegalIdService;
import com.zelenskaya.nestserava.app.service.UpdateNameLegalService;
import com.zelenskaya.nestserava.app.service.UpdateApplicationDetailsService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceNotFoundException;
import com.zelenskaya.nestserava.app.service.model.UpdateNameLegalDTO;
import com.zelenskaya.nestserava.app.service.model.UsernameDTO;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UpdateNameLegalServiceImpl implements UpdateNameLegalService {
    private final ApplicationConvRepository applicationConvRepository;
    private final GenerateLegalIdService generateLegalIdService;
    private final UpdateApplicationDetailsService updateApplicationDetailsService;

    @Override
    @Transactional
    public UpdateNameLegalDTO update(Long id, UpdateNameLegalDTO updateNameLegalDTO, HttpServletRequest request) {
        String nameLegal = updateNameLegalDTO.getNameLegal();
        String applicationConvId = updateNameLegalDTO.getApplicationConvId();
        boolean isAgreeIdAndUuid = applicationConvRepository.
                existsApplicationConvByIdAndApplicationConvId(id, applicationConvId);
        if (isAgreeIdAndUuid) {
            try {
                Long legalId = generateLegalIdService.getId(nameLegal);
                Optional<ApplicationConv> applicationConvByApplicationConvId =
                        applicationConvRepository.findApplicationConvByApplicationConvId(applicationConvId);
                if (applicationConvByApplicationConvId.isPresent()) {
                    ApplicationConv applicationConv = applicationConvByApplicationConvId.get();
                    Long legalIdApplicationConv = applicationConv.getLegalId();
                    if (!legalId.equals(legalIdApplicationConv)) {
                        applicationConv.setLegalId(legalId);
                        ApplicationConv updatedApplicationConv = applicationConvRepository.save(applicationConv);
                        UsernameDTO usernameDTO = updateApplicationDetailsService.updateDate(applicationConv.getId(), request);
                        log.info("Заявка на конверсию " + applicationConvId +
                                " перепривязана к LegalId=" + updatedApplicationConv.getLegalId() +
                                " сотрудником банка " + usernameDTO.getUsername());
                        return updateNameLegalDTO;
                    } else {
                        return null;
                    }
                }
            } catch (FeignException e) {
                throw new NoResultException("Компания с " + nameLegal + " не существует");
            }
        }
        throw new ServiceNotFoundException("Проверьте правильность введённых данных");
    }
}