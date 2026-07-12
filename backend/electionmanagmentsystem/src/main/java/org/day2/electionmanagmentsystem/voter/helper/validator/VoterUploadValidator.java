package org.day2.electionmanagmentsystem.voter.helper.validator;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;
import org.day2.electionmanagmentsystem.common.enums.CsvField;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationError;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationException;
import org.day2.electionmanagmentsystem.common.helper.Normalize;
import org.day2.electionmanagmentsystem.common.helper.ValidationHelper;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;
import org.day2.electionmanagmentsystem.voter.dto.request.VoterCsvRow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@UtilityClass
public class VoterUploadValidator {
    private static final EmailValidator EMAIL_VALIDATOR =
            EmailValidator.getInstance();
    public List<VoterCsvRow> cleanRows(List<VoterCsvRow> rows){
        List<CsvValidationError> validationErrors = new ArrayList<>();
        Map<String, VoterCsvRow> uniqueRows = new LinkedHashMap<>();
        for(VoterCsvRow row: rows){
            if(row.isEmptyRow()){
                continue;
            }
            validateEmail(row, validationErrors);
            validateRequiredFields(row, validationErrors);
        if(!validationErrors.isEmpty()){
            throw new CsvValidationException(validationErrors);
        }
        String key = Normalize.normalizeEmail(row.getEmail());
        uniqueRows.putIfAbsent(key,row);
        }

        return new ArrayList<>(uniqueRows.values());
    }

    private void validateRequiredFields( VoterCsvRow row,List<CsvValidationError> errors){
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
        if(ValidationHelper.isBlank(row.getEmail())){
            errors.add(createError(
                    row,
                    CsvField.EMAIL,
                    "Email is required."
            ));
        }
    }
    private CsvValidationError createError(
            VoterCsvRow row,
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
            VoterCsvRow row,
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
}
