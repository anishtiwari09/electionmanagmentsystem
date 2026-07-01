package org.day2.electionmanagmentsystem.voter.mapper;

import org.day2.electionmanagmentsystem.voter.ElectionVoter;
import org.day2.electionmanagmentsystem.voter.dto.response.ElectionVoterResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElectionVoterMapper {
    public List <ElectionVoterResponse> toResponses(List <ElectionVoter> voters){
       return voters.stream()
                .map(this::toResponse)
                .toList();
    }

    public ElectionVoterResponse toResponse(ElectionVoter voter){
        return ElectionVoterResponse.builder()
                .voterId(voter.getPublicId())
                .userId(voter.getUser().getPublicId())
                .firstName(voter.getUser().getFirstName())
                .lastName(voter.getUser().getLastName())
                .updatedAt(voter.getUpdatedAt())
                .createdAt(voter.getCreatedAt())
                .email(voter.getUser().getEmail())
                .build();
    }
}
