package org.day2.electionmanagmentsystem.electioncandidate.mapper;

import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.electioncandidate.dto.response.ElectionCandidateResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ElectionCandidateMapper {
    public ElectionCandidateResponse toResponse(ElectionCandidate candidate){

        return ElectionCandidateResponse.builder()
                .candidateId(candidate.getPublicId())
                .userId(candidate.getUser().getPublicId())
                .firstName(candidate.getUser().getFirstName())
                .lastName(candidate.getUser().getLastName())
                .fullName(
                        candidate.getUser().getFirstName()
                                + " "
                                + candidate.getUser().getLastName()
                )
                .email(candidate.getUser().getEmail())
                .createdAt(candidate.getCreatedAt())
                .updatedAt(candidate.getUpdatedAt())
                .build();

    }

    public List <ElectionCandidateResponse> toResponses(List <ElectionCandidate> candidates){
        if(candidates.isEmpty())  return Collections.emptyList();;
        return candidates.stream()
                .map(this::toResponse)
                .toList();
    }
}
