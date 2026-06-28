package org.day2.electionmanagmentsystem.election.common.helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;

import java.time.LocalDateTime;

public class ElectionDateValidator implements ConstraintValidator <ValidElectionDates, CreateElectionRequest>{
    @Override
    public boolean isValid(CreateElectionRequest request,
                             ConstraintValidatorContext context){
        if (request == null) {
            return true;
        }
        if (request.getStartAt() == null || request.getEndAt() == null) {
            return true; // @NotNull will handle these
        }
        if (request.getEndAt().isAfter(request.getStartAt())) {
            return true;
        }
        LocalDateTime now =LocalDateTime.now();
        if(now.isAfter(request.getStartAt())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ElectionErrorCode.START_AND_END_DATE_MUST_BE_FUTURE.getMessage()).addPropertyNode("startAt").addConstraintViolation();

            return false;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        ElectionErrorCode.INVALID_START_OR_END_DATE.getMessage())
                .addPropertyNode("endAt")
                .addConstraintViolation();

        return false;
    }
}
