package org.day2.electionmanagmentsystem.common.helper;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
@UtilityClass
public class ValidationHelper {
    private static final EmailValidator EMAIL_VALIDATOR =
            EmailValidator.getInstance();
    static void validateVersion(Long sourceVersion, Long targetVersion){
        if(targetVersion == null) return;

        if(targetVersion.equals(sourceVersion) ) return;

        throw new BusinessException(ErrorCode.INVALID_REQUEST);


    }

    public boolean isBlank(String value){
        return value ==null || value.isBlank();
    }
    public boolean isValidEmail(String email){
        return EMAIL_VALIDATOR.isValid(email);
    }
}
