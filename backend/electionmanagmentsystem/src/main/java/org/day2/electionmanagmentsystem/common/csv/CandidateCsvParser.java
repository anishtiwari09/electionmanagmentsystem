package org.day2.electionmanagmentsystem.common.csv;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.apache.commons.csv.CSVRecord;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Component
public class CandidateCsvParser {
    public List<CandidateCsvRow> parse(MultipartFile file){
       try{
           Reader reader =new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
           CSVParser csvParser=
                   CSVFormat.DEFAULT
                           .builder()
                           .setHeader()
                           .setSkipHeaderRecord(true)
                           .setIgnoreEmptyLines(false)
                           .setTrim(true)
                           .get()
                           .parse(reader);


          List <CandidateCsvRow> rows= new ArrayList<>();

          for(CSVRecord record: csvParser){
              rows.add(
                      CandidateCsvRow
                      .builder()
                              .rowNumber((int) record.getRecordNumber()+1)
                              .firstName(get(record,"first_name"))
                              .lastName(get(record,"last_name"))
                              .email(get(record,"email"))
                              .positionName(get(record,"position_name"))
                              .positionId(parseUUID(get(record,"position_id")))

                              .build()

              );
          }

           return rows;
       }
       catch(Exception e){
        
           return null;
       }

    }

    private String get(CSVRecord record, String header){
        if(!record.isMapped(header))
            return null;
        return record.get(header);

    }
    private UUID parseUUID(String value){
        if (value == null || value.isBlank()) {
            return null;
        }
        try {

            return UUID.fromString(value.trim());

        } catch (IllegalArgumentException exception) {

            return null;
        }

    }
}
