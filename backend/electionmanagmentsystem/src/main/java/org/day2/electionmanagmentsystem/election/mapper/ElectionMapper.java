package org.day2.electionmanagmentsystem.election.mapper;

import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionDetailsResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionResponse;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.dto.response.ElectionPositionResponse;
import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.day2.electionmanagmentsystem.voter.dto.response.ElectionVoterResponse;
import org.day2.electionmanagmentsystem.voter.mapper.ElectionVoterMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElectionMapper {
    public ElectionDetailsResponse toDetailsResponse(Election election, List <ElectionPositionResponse> positions, List<ElectionVoterResponse> voters){
        return ElectionDetailsResponse.builder()
                .electionId(election.getPublicId())
                .name(election.getName())
                .description(election.getDescription())
                .status(election.getStatus())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .createdAt(election.getCreatedAt())
                .updatedAt(election.getUpdatedAt())
                .voters(voters)
                .positions(positions)
                .build();
    }
    public ElectionResponse toElectionResponse(Election election) {

        return ElectionResponse.builder()
                .electionId(election.getPublicId())
                .name(election.getName())
                .description(election.getDescription())
                .status(election.getStatus())
                .startAt(election.getStartAt())
                .endAt(election.getEndAt())
                .createdAt(election.getCreatedAt())
                .updatedAt(election.getUpdatedAt())
                .build();
    }
}
