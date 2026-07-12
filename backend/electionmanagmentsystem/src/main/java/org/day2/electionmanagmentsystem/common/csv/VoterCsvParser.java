package org.day2.electionmanagmentsystem.common.csv;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;
import org.day2.electionmanagmentsystem.voter.dto.request.VoterCsvRow;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Component
public class VoterCsvParser {
    public List<VoterCsvRow> parse(MultipartFile file){
        try{
            Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            CSVParser csvParser =
                    CSVFormat.DEFAULT.builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .setIgnoreEmptyLines(false)
                            .setTrim(true)
                            .get()
                            .parse(reader);

            List <VoterCsvRow> rows= new ArrayList<>();
            for(CSVRecord record: csvParser){
                rows.add(
                        VoterCsvRow.builder()
                                .rowNumber((int) record.getRecordNumber() + 1)
                                .firstName(get(record,"first_name"))
                                .lastName(get(record,"last_name"))
                                .email(get(record,"email"))
                                .build()
                );

            }
            return rows;

        }
        catch(Exception exception){
            return null;
        }
    }
    private String get(CSVRecord record, String header){
        if(!record.isMapped(header))
            return null;
        return record.get(header);

    }
}
