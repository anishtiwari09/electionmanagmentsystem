package org.day2.electionmanagmentsystem.voter.mapper;


import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.voter.dto.response.VoterElectionsResponse;
import org.springframework.stereotype.Component;

@Component
public class VoterElectionMapper {
    public VoterElectionsResponse toVoterElectionsMapper(Election election){


        return VoterElectionsResponse
                .builder()
                .name(election.getName())
                .description(election.getDescription())
                .electionId(election.getPublicId())
                .status(election.getStatus())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .build();
    }
}
