package com.zelenskaya.nestserava.app.service.impl;

import com.zelenskaya.nestserava.app.repository.ApplicationConvRepository;
import com.zelenskaya.nestserava.app.repository.model.ApplicationConv;
import com.zelenskaya.nestserava.app.repository.model.Status;
import com.zelenskaya.nestserava.app.repository.model.ValueInd;
import com.zelenskaya.nestserava.app.repository.model.ValueLeg;
import com.zelenskaya.nestserava.app.service.GetApplicationConvService;
import com.zelenskaya.nestserava.app.service.GetEmployeeByIdService;
import com.zelenskaya.nestserava.app.service.GetLegalsByIdService;
import com.zelenskaya.nestserava.app.service.exceptions.ServiceConversionException;
import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.PaginationDTO;
import com.zelenskaya.nestserava.app.service.model.StatusEnumDTO;
import com.zelenskaya.nestserava.app.service.model.ValueIndEnumDTO;
import com.zelenskaya.nestserava.app.service.model.ValueLegEnumDTO;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetApplicationConvServiceImpl implements GetApplicationConvService {
    private final ApplicationConvRepository applicationConvRepository;
    private final GetEmployeeByIdService getEmployeeByIdService;
    private final GetLegalsByIdService getLegalsByIdService;
    private final ServiceConversionConstants constants;

    @Override
    @Transactional
    public ApplicationConvDTO getById(Long id) {
        try {
            ApplicationConv applicationConv = applicationConvRepository.getById(id);
            return convertToDTO(applicationConv);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<ApplicationConvDTO> get(PaginationDTO paginationDTO) {
        Page<ApplicationConv> applicationConvs;
        try {
            switch (paginationDTO.getPagination()) {
                case DEFAULT: {
                    applicationConvs = getConversion(paginationDTO.getPage(), constants.getDefaultPage());
                    break;
                }
                case CUSTOMIZED: {
                    Integer customizedPage = paginationDTO.getCustomizedPage();
                    List<Integer> defaultPages = Arrays.asList(constants.getDefaultPages());
                    if (defaultPages.contains(customizedPage)) {
                        applicationConvs = getConversion(paginationDTO.getPage(), customizedPage);
                    } else {
                        throw new ServiceConversionException();
                    }
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + paginationDTO.getPagination());
            }
        } catch (NullPointerException | IllegalStateException e) {
            List<ApplicationConv> applicationConvList = applicationConvRepository.findAll();
            applicationConvs = new PageImpl(applicationConvList);
        }
        if (!applicationConvs.isEmpty()) {
            return applicationConvs.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Page<ApplicationConv> getConversion(Integer page, int defaultPage) {
        Pageable pageable = PageRequest.of(page - 1, defaultPage);
        return applicationConvRepository.findAll(pageable);
    }

    private ApplicationConvDTO convertToDTO(ApplicationConv applicationConv) {
        ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();

        Long id = applicationConv.getId();
        applicationConvDTO.setId(id);

        String applicationConvIdLabel = applicationConv.getApplicationConvId();
        UUID applicationConvId = UUID.fromString(applicationConvIdLabel);
        applicationConvDTO.setApplicationConvId(applicationConvId);

        Long legalId = applicationConv.getLegalId();
        String nameLegal = getNameLegalById(legalId);
        applicationConvDTO.setNameLegal(nameLegal);

        Long employeeId = applicationConv.getEmployeeId();
        String nameEmployee = getNameEmployeeById(employeeId);
        applicationConvDTO.setNameEmployee(nameEmployee);

        ValueLeg valueLeg = applicationConv.getValueLeg();
        ValueLegEnumDTO valueLegEnumDTO = getValueLegEnumDTO(valueLeg);
        applicationConvDTO.setValueLeg(valueLegEnumDTO);

        ValueInd valueInd = applicationConv.getValueInd();
        ValueIndEnumDTO valueIndEnumDTO = getValueIndEnumDTO(valueInd);
        applicationConvDTO.setValueInd(valueIndEnumDTO);

        Status status = applicationConv.getStatus();
        StatusEnumDTO statusEnumDTO = getStatusEnumDTO(status);
        applicationConvDTO.setStatus(statusEnumDTO);

        Float percentConv = applicationConv.getPercentConv();
        applicationConvDTO.setPercent(percentConv);

        return applicationConvDTO;
    }

    private String getNameEmployeeById(Long employeeId) {
        try {
            ResponseEntity<Object> responseEntity = getEmployeeByIdService.getEmployeeById(employeeId);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List entityBody = (List) responseEntity.getBody();
                assert entityBody != null;
                Map<String, Object> body = (Map<String, Object>) entityBody.get(0);
                return body.get("Full_Name_Individual").toString();
            } else {
                throw new NoResultException("Указанного сотрудника в компании не найдено");
            }
        } catch (FeignException e) {
            throw new NoResultException("Указанного сотрудника в компании не найдено");
        }
    }

    private String getNameLegalById(Long legalId) {
        try {
            ResponseEntity<Object> responseEntity = getLegalsByIdService.getLegalById(legalId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List entityBody = (List) responseEntity.getBody();
                assert entityBody != null;
                Map<String, Object> body = (Map<String, Object>) entityBody.get(0);
                return body.get("Name_Legal").toString();
            } else {
                throw new NoResultException("Указаной компании не найдено");
            }
        } catch (FeignException e) {
            throw new NoResultException("Указаной компании не найдено");
        }
    }

    private StatusEnumDTO getStatusEnumDTO(Status status) {
        String statusLabel = status.getStatus().name();
        return StatusEnumDTO.valueOf(statusLabel);
    }

    private ValueIndEnumDTO getValueIndEnumDTO(ValueInd valueInd) {
        String valueIndLabel = valueInd.getValueInd().name();
        return ValueIndEnumDTO.valueOf(valueIndLabel);
    }

    private ValueLegEnumDTO getValueLegEnumDTO(ValueLeg valueLeg) {
        String valueLegLabel = valueLeg.getValueLeg().name();
        return ValueLegEnumDTO.valueOf(valueLegLabel);
    }
}