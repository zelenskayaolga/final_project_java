package com.zelenskaya.nestserava.app.service.util;

import com.zelenskaya.nestserava.app.service.model.ApplicationConvDTO;
import com.zelenskaya.nestserava.app.service.model.ValueIndEnumDTO;
import com.zelenskaya.nestserava.app.service.model.ValueLegEnumDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class CvsUtil {
    public static String TYPE = "text/csv";

    public boolean isCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType())
                || Objects.equals(file.getContentType(), MediaType.MULTIPART_FORM_DATA_VALUE);
    }

    public List<ApplicationConvDTO> readCVSFile(InputStream inputStream) {
        CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, format)) {
            List<ApplicationConvDTO> applicationTutorialList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ApplicationConvDTO applicationConvDTO = new ApplicationConvDTO();
                applicationConvDTO.setApplicationConvId(UUID.fromString(csvRecord.get("ApplicationConvId")));
                applicationConvDTO.setNameLegal(csvRecord.get("NameLegal"));
                applicationConvDTO.setNameEmployee(csvRecord.get("Full_Name_Individual"));
                applicationConvDTO.setValueLeg(ValueLegEnumDTO.valueOf(csvRecord.get("Value_Leg")));
                applicationConvDTO.setValueInd(ValueIndEnumDTO.valueOf(csvRecord.get("Value_Ind")));
                applicationConvDTO.setPercent(Float.parseFloat(csvRecord.get("Percent_Conv")));
                applicationTutorialList.add(applicationConvDTO);
            }
            return applicationTutorialList;
        } catch (IOException e) {
            throw new NoResultException("Не удаётся проанвлизировать CVS-файл");
        }
    }
}
