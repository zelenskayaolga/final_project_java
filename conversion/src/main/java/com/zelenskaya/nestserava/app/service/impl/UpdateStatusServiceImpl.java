package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.ApplicationConvRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.service.UpdateApplicationDetailsService;
import com.zelenskaya.nestserava.app.service.UpdateStatusService;
import com.zelenskaya.nestserava.app.service.exceptions.NotAcceptableException;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.UpdateStatusDTO;
import com.zelenskaya.nestserava.app.service.model.UpdatedStatusDTO;
import com.zelenskaya.nestserava.app.service.model.UsernameDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateStatusServiceImpl implements UpdateStatusService {
    private final ApplicationConvRepository applicationConvRepository;
    private final StatusRepository statusRepository;
    private final UpdateApplicationDetailsService updateApplicationDetailsService;

    @Override
    @Transactional
    public UpdatedStatusDTO update(UpdateStatusDTO updateStatusDTO, HttpServletRequest request) {
        String applicationConvId = updateStatusDTO.getApplicationConvId();
        StatusEnumDTO newStatus = updateStatusDTO.getStatus();
        Optional<ApplicationConv> applicationConvByApplicationConvId = applicationConvRepository.
                findApplicationConvByApplicationConvId(applicationConvId);
        if (applicationConvByApplicationConvId.isPresent()) {
            ApplicationConv applicationConv = applicationConvByApplicationConvId.get();
            StatusEnum statusNow = applicationConv.getStatus().getStatus();
            Status status = generateStatus(newStatus, statusNow);
            applicationConv.setStatus(status);
            ApplicationConv updatedApplicationStatus = applicationConvRepository.save(applicationConv);
            Long id = updatedApplicationStatus.getId();
            UsernameDTO usernameDTO = updateApplicationDetailsService.updateDate(id, request);
            return convertToDTO(updatedApplicationStatus, usernameDTO);
        } else {
            throw new NoResultException("Заявление на конверсию от сотрудника не существует");
        }
    }

    private UpdatedStatusDTO convertToDTO(ApplicationConv updatedApplicationStatus,
                                          UsernameDTO usernameDTO) {
        UpdatedStatusDTO updatedStatusDTO = new UpdatedStatusDTO();
        String statusString = updatedApplicationStatus.getStatus().getStatus().name();
        updatedStatusDTO.setStatus(StatusEnumDTO.valueOf(statusString));
        updatedStatusDTO.setUsername(usernameDTO.getUsername());
        return updatedStatusDTO;
    }

    private Status generateStatus(StatusEnumDTO newStatusEnumDTO, StatusEnum statusEnumNow) {
        Status newStatus = generateNewStatus(newStatusEnumDTO);
        String stringNewStatus = newStatusEnumDTO.name();
        switch (statusEnumNow) {
            case NEW:
                if (stringNewStatus.equals(StatusEnumDTO.IN_PROGRESS.name()) ||
                        stringNewStatus.equals(StatusEnumDTO.REJECTED.name())) {
                    return newStatus;
                } else {
                    throw new NotAcceptableException("Статус не может быть изменен");
                }
            case IN_PROGRESS:
                if (stringNewStatus.equals(StatusEnumDTO.DONE.name())) {
                    return newStatus;
                } else {
                    throw new NotAcceptableException("Статус не может быть изменен");
                }
            default:
                throw new NotAcceptableException("Статус не может быть изменен");
        }
    }

    private Status generateNewStatus(StatusEnumDTO newStatusEnumDTO) {
        String stringStatus = newStatusEnumDTO.name();
        StatusEnum statusEnum = StatusEnum.valueOf(stringStatus);
        Optional<Status> status = statusRepository.findStatusByStatus(statusEnum);
        if (status.isPresent()) {
            return status.get();
        } else {
            throw new NoResultException("Укажите правильно статус");
        }
    }
}
