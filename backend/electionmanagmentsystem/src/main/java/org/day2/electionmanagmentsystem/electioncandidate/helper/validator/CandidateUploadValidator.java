package org.day2.electionmanagmentsystem.electioncandidate.helper.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;
import org.day2.electionmanagmentsystem.common.enums.CsvField;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationError;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationException;
import org.day2.electionmanagmentsystem.common.helper.ValidationHelper;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@UtilityClass
public class CandidateUploadValidator {
    private static final EmailValidator EMAIL_VALIDATOR =
            EmailValidator.getInstance();
    public List<CandidateCsvRow> cleanRows(List<CandidateCsvRow> rows){
        List<CsvValidationError> validationErrors = new ArrayList<>();

        Map<String, CandidateCsvRow> uniqueRows = new LinkedHashMap<>();

        for(CandidateCsvRow row : rows){
            if(row.isEmptyRow()){
                continue;
            }
            validateRequiredFields(row, validationErrors);
            validateEmail(row, validationErrors);

            if(!hasValidationError(validationErrors, row.getRowNumber())){
                String duplicateKey =
                        row.getEmail().trim().toLowerCase()
                                + "|"
                                + row.getPositionId();
                uniqueRows.putIfAbsent(
                        duplicateKey,
                        row
                );
            }
        }
        if (!validationErrors.isEmpty()) {
            throw new CsvValidationException(validationErrors);
        }
        return new ArrayList<>(uniqueRows.values());
    }
    private void validateRequiredFields(
            CandidateCsvRow row,
            List<CsvValidationError> errors
    ){
        if(ValidationHelper.isBlank(row.getFirstName())){
            errors.add(createError(
                    row,
                    CsvField.FIRST_NAME,
                    "First name is required."
            ));
        }
        if(ValidationHelper.isBlank(row.getLastName())){
            errors.add(createError(
                    row,
                    CsvField.LAST_NAME,
                    "Last name is required."
            ));
        }
        if(ValidationHelper.isBlank(row.getPositionName())){
            errors.add(createError(
                    row,
                    CsvField.POSITION_NAME,
                    "Position name is required."
            ));
        }
        if(row.getPositionId()==null){
            errors.add(createError(
                    row,
                    CsvField.POSITION_ID,
                    "Position Id is required."
            ));
        }
        if(ValidationHelper.isBlank(row.getEmail())){
            errors.add(createError(
                    row,
                    CsvField.EMAIL,
                    "Email is required."
            ));
        }

    }
    private CsvValidationError createError(
            CandidateCsvRow row,
            CsvField field,
            String message
    ) {

        return CsvValidationError.builder()
                .row(row.getRowNumber())
                .field(field)
                .message(message)
                .build();
    }
    private void validateEmail(
            CandidateCsvRow row,
            List<CsvValidationError> errors
    ) {

        if (ValidationHelper.isBlank(row.getEmail())) {
            return;
        }

        if (!ValidationHelper.isValidEmail(row.getEmail())) {

            errors.add(createError(
                    row,
                    CsvField.EMAIL,
                    "Invalid email address."
            ));
        }
    }
    private boolean hasValidationError(
            List<CsvValidationError> errors,
            int rowNumber
    ) {

        return errors.stream()
                .anyMatch(error -> error.getRow() == rowNumber);
    }

}
