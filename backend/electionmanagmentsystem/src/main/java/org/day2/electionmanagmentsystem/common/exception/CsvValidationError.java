package org.day2.electionmanagmentsystem.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.day2.electionmanagmentsystem.common.enums.CsvField;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CsvValidationError {
    private Integer row;

    private CsvField field;

    private String message;
    private String positionName;
    private UUID positionId;

}
