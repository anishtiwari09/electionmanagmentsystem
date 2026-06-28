package org.day2.electionmanagmentsystem.election.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetElectionsRequest {
    private List<ElectionStatus> status;
    private String search;
    private Integer page = 0;
    private Integer size = 10;
}
