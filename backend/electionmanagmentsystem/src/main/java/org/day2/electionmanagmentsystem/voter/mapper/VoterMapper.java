package org.day2.electionmanagmentsystem.voter.mapper;

import org.day2.electionmanagmentsystem.voter.Voter;
import org.day2.electionmanagmentsystem.voter.dto.response.VoterResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoterMapper {
    public List <VoterResponse> toResponses(List <Voter> voters){
       return voters.stream()
                .map(this::toResponse)
                .toList();
    }

    public VoterResponse toResponse(Voter voter){
        return VoterResponse.builder()
                .id(voter.getPublicId())
                .userId(voter.getUser().getPublicId())
                .firstName(voter.getUser().getFirstName())
                .lastName(voter.getUser().getLastName())
                .updatedAt(voter.getUpdatedAt())
                .createdAt(voter.getCreatedAt())
                .email(voter.getUser().getEmail())
                .build();
    }

}
