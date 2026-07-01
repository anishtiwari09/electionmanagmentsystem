package org.day2.electionmanagmentsystem.position.Helper;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ElectionPositonRequestValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateCreateElectionPositionRequest {

    String message() default "Invalid election positions";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}