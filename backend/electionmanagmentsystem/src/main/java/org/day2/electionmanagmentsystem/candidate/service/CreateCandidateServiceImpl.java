package org.day2.electionmanagmentsystem.candidate.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class CreateCandidateServiceImpl implements CreateCandidateService{
    final private ElectionRepository electionRepository;
    @Override
    public UUID createCandidate(UUID electionPublicId, UUID userPublicId, CreateCandidateService request) {
        Election election = electionRepository
                .findByPublicIdAndUserPublicId(
                        electionPublicId,
                        userPublicId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.ELECTION_NOT_FOUND
                        )
                );
        if (election.getStatus() != ElectionStatus.DRAFT) {
            throw new BusinessException(
                    ElectionErrorCode.NOT_ALLOWED
            );
        }
//        validateCreateCandidateRequest(request);
        return null;
    }
}
