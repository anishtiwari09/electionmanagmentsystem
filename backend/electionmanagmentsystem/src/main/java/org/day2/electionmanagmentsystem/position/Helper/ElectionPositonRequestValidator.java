package org.day2.electionmanagmentsystem.position.Helper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.PositionErrorCode;
import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElectionPositonRequestValidator implements
        ConstraintValidator<ValidateCreateElectionPositionRequest, List<CreateElectionPositionRequest>> {

    private static final String POSITION_NAME_REGEX =
            "^[A-Za-z0-9]+(?: [A-Za-z0-9]+)*$";

    @Override
    public boolean isValid(List<CreateElectionPositionRequest> requests,
                           ConstraintValidatorContext context) {

        if (requests == null || requests.isEmpty()) {
            return true;
        }

        Set<String> uniquePositionNames = new HashSet<>();

        for (int i = 0; i < requests.size(); i++) {

            CreateElectionPositionRequest request = requests.get(i);

            if (request == null) {
                continue;
            }

            String positionName = request.getPositionName();

            if (positionName != null && !positionName.isBlank()) {

                // Normalize for duplicate check
                String normalizedName = positionName
                        .trim()
                        .replaceAll("\\s+", " ")
                        .toLowerCase();

                if (!uniquePositionNames.add(normalizedName)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                                    PositionErrorCode.DUPLICATE_POSITION_NAME.getMessage())
                            .addPropertyNode("positionName")
                            .inIterable()
                            .atIndex(i)
                            .addConstraintViolation();

                    return false;
                }
            }

            if (!validateElectionPositionRequest(request, context, i)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateElectionPositionRequest(CreateElectionPositionRequest request,
                                                    ConstraintValidatorContext context,
                                                    int index) {

        if (request == null) {
            return true;
        }

        if (request.getMinSelection() > request.getMaxSelection()) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            PositionErrorCode.INVALID_MIN_MAX_SELECTION.getMessage())
                    .addPropertyNode("maxSelection")
                    .inIterable()
                    .atIndex(index)
                    .addConstraintViolation();

            return false;
        }

        String positionName = request.getPositionName();

        if (positionName != null && !positionName.isBlank()) {

            positionName = positionName
                    .trim()
                    .replaceAll("\\s+", " ");

            if (!positionName.matches(POSITION_NAME_REGEX)) {

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                PositionErrorCode.INVALID_ELECTION_POSITION_NAME.getMessage())
                        .addPropertyNode("positionName")
                        .inIterable()
                        .atIndex(index)
                        .addConstraintViolation();

                return false;
            }
        }

        return true;
    }
}