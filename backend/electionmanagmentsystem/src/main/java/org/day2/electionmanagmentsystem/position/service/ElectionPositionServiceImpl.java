package org.day2.electionmanagmentsystem.position.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.PositionErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.Helper.ElectionPositonHelper;
import org.day2.electionmanagmentsystem.position.dto.request.CreateElectionPositionRequest;
import org.day2.electionmanagmentsystem.position.dto.request.CreatePositonsRequest;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElectionPositionServiceImpl implements ElectionPositionService{

    private final ElectionRepository electionRepository;
    private final ElectionPositionRepository electionPositionRepository;

    @Override
    public UUID createPositions(UUID electionPublicId, UUID userPublicId, CreatePositonsRequest requests) {

        Election election = electionRepository.findByPublicIdAndUserPublicId(electionPublicId,userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        if (election.getStatus() != ElectionStatus.DRAFT) {
            throw new BusinessException(
                    ElectionErrorCode.ELECTION_IS_NOT_EDITABLE
            );
        }
        Set<String> requestedNames = requests.getPositions().stream()
                .map(CreateElectionPositionRequest::getPositionName)
                .map(name -> name.trim().replaceAll("\\s+", " ").toLowerCase())
                .collect(Collectors.toSet());

        List<String> existingNames =
                electionPositionRepository.findExistingPositionNames(election.getId(), requestedNames);

        if (!existingNames.isEmpty()) {
            throw new RuntimeException(
                    PositionErrorCode.POSITION_ALREADY_EXISTS.getMessage()
                            + " Existing position(s): "
                            + String.join(", ", existingNames)
            );
        }

        List <ElectionPosition> positions= requests.getPositions().stream().map(request ->
                ElectionPosition.builder()
                        .name(request.getPositionName().trim().replaceAll("\\s+", " ").toLowerCase())
                        .description(request.getDescription().trim())
                        .minSelection(request.getMinSelection())
                        .maxSelection(request.getMaxSelection())
                        .election(election)
                        .build()).toList();

        positions=electionPositionRepository.saveAll(positions);
        return positions.get(0).getPublicId();

    }
}
