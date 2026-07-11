package org.day2.electionmanagmentsystem.common.helper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Normalize {
    public String normalizeEmail(String email){
        return email.trim().toLowerCase();
    }
}
