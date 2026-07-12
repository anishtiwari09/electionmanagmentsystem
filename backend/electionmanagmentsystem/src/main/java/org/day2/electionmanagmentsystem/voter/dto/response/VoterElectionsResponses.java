package org.day2.electionmanagmentsystem.voter.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VoterElectionsResponses {
    private List<VoterElectionsResponse> elections;
    private long totalElement ;
    private int totalPages;
    private int page;
    private int size;
}
