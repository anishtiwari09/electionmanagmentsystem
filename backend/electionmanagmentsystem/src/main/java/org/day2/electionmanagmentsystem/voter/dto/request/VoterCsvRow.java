package org.day2.electionmanagmentsystem.voter.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VoterCsvRow {
    private int rowNumber;
    private String firstName;
    private String lastName;
    private String email;

    public boolean isEmptyRow(){
        return isBlank(firstName)&& isBlank(lastName)&&isBlank(email);
    }
    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }
}
