package org.day2.electionmanagmentsystem.common.csv;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class CsvTemplateGenerator {
    public byte[] voterTemplate(){
        try{
            ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
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
                                    "email"

                            ).get()

            );
            csvPrinter.flush();
            return outputStream.toByteArray();
        }

        catch (Exception exception){
            throw new RuntimeException(
                    "Unable to generate Voter template.",
                    exception
            );
        }
    }

}
