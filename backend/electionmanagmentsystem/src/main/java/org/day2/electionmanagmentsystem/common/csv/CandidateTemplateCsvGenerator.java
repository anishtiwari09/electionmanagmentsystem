package org.day2.electionmanagmentsystem.common.csv;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateTemplatePositionRequest;
import org.day2.electionmanagmentsystem.position.ElectionPosition;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class CandidateTemplateCsvGenerator {
    public byte[] generate( List<ElectionPosition> electionPositions,
                          Map<UUID, CandidateTemplatePositionRequest> requestPositionMap){


        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(
                    outputStream,
                    StandardCharsets.UTF_8
            );
            CSVPrinter csvPrinter = new CSVPrinter(
                    writer,
                    CSVFormat.DEFAULT.builder()
                            .setHeader(
                                    "first_name",
                                    "last_name",
                                    "email",
                                    "position_name",
                                    "position_id"
                            )
                            .build()
            );
            for (ElectionPosition electionPosition : electionPositions) {

                CandidateTemplatePositionRequest requestPosition =
                        requestPositionMap.get(
                                electionPosition.getPublicId()
                        );

                for (int i = 0;
                     i < requestPosition.getNumberOfCandidates();
                     i++) {

                    csvPrinter.printRecord(
                            "",
                            "",
                            "",
                            electionPosition.getName(),
                            electionPosition.getPublicId()
                    );
                }
            }

            csvPrinter.flush();
              return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Unable to generate candidate template.",
                    exception
            );
        }


    }
}
