package org.day2.electionmanagmentsystem.election.dto.request;

import lombok.Data;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;

import java.util.UUID;
@Data
public class ChangeElectionStatusRequest {
    private Long version;
    private UUID electionPublicId;
    private ElectionStatus electionStatus;

}
