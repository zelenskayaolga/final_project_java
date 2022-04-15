package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.service.util.CvsUtil;
import com.zelenskaya.nestserava.app.repository.ApplicationConvRepository;
import com.zelenskaya.nestserava.app.repository.StatusRepository;
import com.zelenskaya.nestserava.app.repository.ValueIndRepository;
import com.zelenskaya.nestserava.app.repository.ValueLegRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import com.zelenskaya.nestserava.app.repository.model.ApplicationDetails;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.StatusEnum;
import com.zelenskaya.nestserava.app.repository.model.ValueInd;
import com.zelenskaya.nestserava.app.repository.model.ValueIndEnum;
import com.zelenskaya.nestserava.app.repository.model.ValueLeg;
import com.zelenskaya.nestserava.app.repository.model.ValueLegEnum;
import com.zelenskaya.nestserava.app.service.CvsAddService;
import com.zelenskaya.nestserava.app.service.GenerateLegalIdService;
import com.zelenskaya.nestserava.app.service.GetEmployeeByNameService;
import com.zelenskaya.nestserava.app.service.LocalDateService;
import com.zelenskaya.nestserava.app.service.config.ServiceConfig;
import com.zelenskaya.nestserava.app.service.exceptions.ConflictException;
import com.zelenskaya.nestserava.app.service.model.AddedApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.ValueIndEnumDTO;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CvsAddServiceImpl implements CvsAddService {
    private final ApplicationConvRepository applicationConvRepository;
    private final CvsUtil cvsHelper;
    private final ValueIndRepository valueIndRepository;
    private final ValueLegRepository valueLegDetailsRepository;
    private final GenerateLegalIdService generateLegalId;
    private final StatusRepository statusRepository;
    private final LocalDateService localDateService;
    private final ServiceConfig serviceConfig;
    private final GetEmployeeByNameService getEmployeeByNameService;

    @Override
    public List<AddedApplicationConvDTO> addApplicationConv(MultipartFile file) {
        try {
            List<ApplicationConvDTO> applicationTutorialList = cvsHelper.readCVSFile(file.getInputStream());
            List<ApplicationConv> applicationsConv = convertToEntity(applicationTutorialList);
            ApplicationDetails applicationDetails = generateApplicationDetails(serviceConfig.getZoneTime());
            for (ApplicationConv applicationConv : applicationsConv) {
                applicationConv.setApplicationDetails(applicationDetails);
                applicationDetails.setApplicationConv(applicationConv);
            }
            try {
                applicationConvRepository.saveAll(applicationsConv);
                return convertToDTOList(applicationsConv);
            } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                throw new ConflictException("Файл содержит дубль заявки ");
            }
        } catch (IOException e) {
            throw new NoResultException("Не удается проанализировать CSV-файл");
        }
    }

    private List<AddedApplicationConvDTO> convertToDTOList(List<ApplicationConv> applicationsConv) {
        List<AddedApplicationConvDTO> applicationsConvDTO = new ArrayList<>();
        for (ApplicationConv applicationConv : applicationsConv) {
            AddedApplicationConvDTO applicationConvDTO = new AddedApplicationConvDTO();
            applicationConvDTO.setApplicationConvId(applicationConv.getApplicationConvId());
            applicationsConvDTO.add(applicationConvDTO);
        }
        return applicationsConvDTO;
    }

    private List<ApplicationConv> convertToEntity(List<ApplicationConvDTO> tutorials) {
        List<ApplicationConv> applicationsConv = new ArrayList<>();
        for (ApplicationConvDTO applicationConvDTO : tutorials) {
            ApplicationConv applicationConv = new ApplicationConv();
            applicationConv.setId(applicationConv.getId());
            UUID applicationConvId = applicationConvDTO.getApplicationConvId();
            applicationConv.setApplicationConvId(String.valueOf(applicationConvId));
            Long legalId = generateLegalId.getId(applicationConvDTO.getNameLegal());
            applicationConv.setLegalId(legalId);
            Long employeeId = generateEmployeeId(applicationConvDTO);
            applicationConv.setEmployeeId(employeeId);
            ValueLeg valueLeg = generateValueLeg(applicationConvDTO);
            applicationConv.setValueLeg(valueLeg);
            ValueIndEnumDTO valueIndDTO = applicationConvDTO.getValueInd();
            ValueInd valueInd = generateValueInd(valueIndDTO);
            applicationConv.setValueInd(valueInd);
            Status status = generateStatus();
            applicationConv.setStatus(status);
            applicationConv.setPercentConv(applicationConvDTO.getPercent());
            applicationsConv.add(applicationConv);
        }
        return applicationsConv;
    }

    private Long generateEmployeeId(ApplicationConvDTO applicationConvDTO) {
        try {
            String nameEmployee = applicationConvDTO.getNameEmployee();
            ResponseEntity<Object> responseEntity = getEmployeeByNameService.getEmployeeByName(nameEmployee);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> body = (List<Map<String, Object>>) responseEntity.getBody();
                assert body != null;
                List<String> namesLegals = generateListNamesLegals(body);
                for (String namesLegal : namesLegals) {
                    for (int i = 0; i < namesLegals.size(); i++) {
                        if (namesLegal.equals(applicationConvDTO.getNameLegal())) {
                            return Long.parseLong(body.get(0).get("EmployeeId").toString());
                        }
                    }
                }
            }
            throw new NoResultException("Указанного сотрудника в компании не найдено");
        } catch (FeignException e) {
            throw new NoResultException("Указанного сотрудника в компании не найдено");
        }
    }

    private List<String> generateListNamesLegals(List<Map<String, Object>> body) {
        List<String> namesLegals = new ArrayList<>();
        for (Map<String, Object> stringStringMap : body) {
            String nameLegal = (String) stringStringMap.get("Name_Legal");
            namesLegals.add(nameLegal);
        }
        return namesLegals;
    }

    private Status generateStatus() {
        Optional<Status> status = statusRepository.findStatusByStatus(StatusEnum.NEW);
        return status.orElse(null);
    }

    private ValueLeg generateValueLeg(ApplicationConvDTO applicationConvDTO) {
        String valueLegString = applicationConvDTO.getValueLeg().name();
        Optional<ValueLeg> valueLegDetailsByValueLeg = valueLegDetailsRepository.
                findByValueLeg(ValueLegEnum.valueOf(valueLegString));
        return valueLegDetailsByValueLeg.orElse(null);
    }

    private ValueInd generateValueInd(ValueIndEnumDTO valueIndEnumDTO) {
        final String name = valueIndEnumDTO.name();
        ValueIndEnum valueIndEnum = ValueIndEnum.valueOf(name);
        Optional<ValueInd> valueInd = valueIndRepository.findByValueInd(valueIndEnum);
        return valueInd.orElse(null);
    }

    private ApplicationDetails generateApplicationDetails(String timeZone) {
        ApplicationDetails applicationDetails = new ApplicationDetails();
        LocalDate createDate = localDateService.dateTimeWithZone(timeZone);
        applicationDetails.setCreateDate(createDate);
        applicationDetails.setUpdateDate(createDate);
        return applicationDetails;
    }
}

