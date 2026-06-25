package org.day2.electionmanagmentsystem.position.dto.request;

import lombok.Getter;

@Getter
public class CreateElectionPositionRequest {
    public String name;
    public String description;
    public int minSelection;
    public int maxSelection;

}
