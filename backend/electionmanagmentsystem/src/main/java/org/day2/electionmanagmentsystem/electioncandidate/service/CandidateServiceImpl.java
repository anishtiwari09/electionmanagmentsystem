package org.day2.electionmanagmentsystem.electioncandidate.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.csv.CandidateTemplateCsvGenerator;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.CandidateErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.PositionErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateTemplatePositionRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.GenerateCandidateTemplateRequest;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateServiceImpl implements CandidateService{
    private final ElectionRepository electionRepository;
    private final ElectionPositionRepository electionPositionRepository;

    private final int MAX_ALLOWED_CANDIDATES=500;

    @Override
    public UUID createCandidate(UUID electionPublicId, UUID userPublicId, CreateElectionCandidateRequest request) {
        return null;
    }

    @Override
    public byte[] generateTemplate(UUID userId, GenerateCandidateTemplateRequest request) {
        UUID electionId = request.getElectionId();
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));

        if(election.getStatus() != ElectionStatus.DRAFT)
        {
            throw new BusinessException(ElectionErrorCode.ELECTION_IS_NOT_EDITABLE);
        }
        Map<UUID, CandidateTemplatePositionRequest> requestPositionMap =
                request.getPositions()
                        .stream()
                        .collect(Collectors.toMap(
                                CandidateTemplatePositionRequest::getElectionPositionId,
                                Function.identity(),
                                (existing, duplicate) -> existing,
                                LinkedHashMap::new
                        ));

        List<ElectionPosition> electionPositions =
                electionPositionRepository.findByElection(election);

        if(electionPositions.isEmpty()) throw new BusinessException(PositionErrorCode.NO_POSITION_FOUND);

        if (electionPositions.size() != requestPositionMap.size()) {
            throw new BusinessException(
                    PositionErrorCode.INVALID_POSITION_REQUEST
            );
        }

        // 4. Validate against database positions
        for (ElectionPosition electionPosition : electionPositions) {

            CandidateTemplatePositionRequest requestPosition =
                    requestPositionMap.get(electionPosition.getPublicId());

            if (requestPosition == null) {
                throw new BusinessException(
                        PositionErrorCode.POSITION_NOT_FOUND
                );
            }

            if (requestPosition.getNumberOfCandidates()
                    < electionPosition.getMaxSelection()) {

                throw new BusinessException(
                        CandidateErrorCode.INVALID_CANDIDATE_COUNT
                );
            }

            if (requestPosition.getNumberOfCandidates()
                    > MAX_ALLOWED_CANDIDATES) {

                throw new BusinessException(
                        CandidateErrorCode.INVALID_CANDIDATE_COUNT
                );
            }
        }

        return CandidateTemplateCsvGenerator.generate(
                electionPositions,
                requestPositionMap
        );
    }



}
