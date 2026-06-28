package org.day2.electionmanagmentsystem.election.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElectionsResponse {
    private List<ElectionResponse> elections;
    private long totalElement ;
    private int totalPages;
    private int page;
    private int size;
}
