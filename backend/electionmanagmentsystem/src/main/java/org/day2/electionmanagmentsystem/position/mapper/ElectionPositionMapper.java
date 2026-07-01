package org.day2.electionmanagmentsystem.position.mapper;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.electioncandidate.dto.response.ElectionCandidateResponse;
import org.day2.electionmanagmentsystem.electioncandidate.mapper.ElectionCandidateMapper;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.dto.response.ElectionPositionResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElectionPositionMapper {
    private final ElectionCandidateMapper electionCandidateMapper;
    public List<ElectionPositionResponse> toResponses(List<ElectionPosition> positions,
                                                           List<ElectionCandidate> candidates){

        if(positions.isEmpty()) return Collections.emptyList();

        Map<Long, List<ElectionCandidate>> candidateMap =
                candidates.stream()
                        .collect(Collectors.groupingBy(
                                candidate -> candidate.getPosition().getId()
                        ));
        return positions.stream()
                .map(position -> {

                    List<ElectionCandidateResponse> candidateResponses =
                            electionCandidateMapper.toResponses(
                                    candidateMap.getOrDefault(
                                            position.getId(),
                                            List.of()
                                    )
                            );

                    return ElectionPositionResponse.builder()
                            .electionPositionId(position.getPublicId())
                            .positionName(position.getName())
                            .maxSelection(position.getMaxSelection())
                            .minSelection(position.getMinSelection())
                            .candidates(candidateResponses)
                            .createdAt(position.getCreatedAt())
                            .updatedAt(position.getUpdatedAt())
                            .build();
                })
                .toList();

    }
}
