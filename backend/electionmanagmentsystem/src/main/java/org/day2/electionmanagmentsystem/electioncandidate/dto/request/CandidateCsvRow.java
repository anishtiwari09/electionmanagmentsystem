package org.day2.electionmanagmentsystem.electioncandidate.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CandidateCsvRow {

    private int rowNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String positionName;

    private UUID positionId;

    /**
     * Returns true when the whole row is empty.
     * Such rows will simply be ignored.
     */
    public boolean isEmptyRow() {

        return isBlank(firstName)
                && isBlank(lastName)
                && isBlank(email)
                && isBlank(positionName)
                && positionId == null;
    }

    /**
     * Returns true when user started filling the row
     * but left one or more required fields empty.
     */
    public boolean isPartialRow() {

        return !isEmptyRow()
                && (
                isBlank(firstName)
                        || isBlank(lastName)
                        || isBlank(email)
                        || isBlank(positionName)
                        || positionId == null
        );
    }

    /**
     * Used for duplicate removal.
     * Duplicate candidate means same email for same position.
     */
    public String duplicateKey() {

        return email.trim().toLowerCase()
                + "|"
                + positionId;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}