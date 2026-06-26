package org.day2.electionmanagmentsystem.candidate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.candidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.candidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.candidate.helper.CandidateHelper;
import org.day2.electionmanagmentsystem.candidate.repo.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.enums.UserStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class CreateCandidateServiceImpl implements CreateCandidateService{
    final private ElectionRepository electionRepository;
    private final UserRepository userRepository;
    private final ElectionCandidateRepository electionCandidateRepository;
    private final CreateCandidateService createCandidateService;
    private final ElectionPositionRepository electionPositionRepository;

    private User createCandidateUser(CreateElectionCandidateRequest request){
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(null);

        user.setPhone(request.getPhone());
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);

    }

    private User getOrCreateCandidate(CreateElectionCandidateRequest request){
        User user =userRepository.findByEmailIgnoreCase(request.getEmail().trim()).orElseGet(()->createCandidateUser(request));
        return user;
    }
    @Override
    public UUID createCandidate(UUID electionPublicId, UUID userPublicId, @Valid @RequestBody CreateElectionCandidateRequest request) {
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
        User candidateUser = getOrCreateCandidate(request);
        ElectionPosition position =
                CandidateHelper.getElectionPosition(
                        electionPositionRepository,
                        election,
                        request.getPositionPublicId()
                );
        CandidateHelper.validateDuplicateCandidate(electionCandidateRepository,position,candidateUser);
        ElectionCandidate electionCandidate =
                saveElectionCandidate(
                        position,
                        candidateUser
                );
        return electionCandidate.getPublicId();

    }

    private ElectionCandidate saveElectionCandidate(
            ElectionPosition position,
            User user
    ) {

        ElectionCandidate electionCandidate =
                new ElectionCandidate();

        electionCandidate.setPosition(
                position
        );

        electionCandidate.setUser(
                user
        );

        return electionCandidateRepository.save(
                electionCandidate
        );
    }
}
