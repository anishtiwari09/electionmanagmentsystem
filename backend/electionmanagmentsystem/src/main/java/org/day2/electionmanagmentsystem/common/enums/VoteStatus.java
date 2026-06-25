package org.day2.electionmanagmentsystem.common.enums;

public enum VoteStatus {
    INVALIDATED,
    DRAFT,
    REVOKED_FOR_REVOTE,
    PENDING_VERIFICATION,
    SUBMITTED;
    public boolean isAllowForVoting(){
        return switch(this){
            case REVOKED_FOR_REVOTE,DRAFT -> true;

            default -> false;

        };
    }
}
