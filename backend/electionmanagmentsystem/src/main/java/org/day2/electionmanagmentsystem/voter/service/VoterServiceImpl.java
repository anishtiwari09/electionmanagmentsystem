package org.day2.electionmanagmentsystem.voter.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.csv.CsvTemplateGenerator;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.voter.repo.VoterRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoterServiceImpl implements VoterService{
    final private VoterRepository voterRepository;
    final private ElectionRepository electionRepository;
    @Override
    public byte[] generateTemplate(UUID electionId, UUID userId){
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        if(election.getStatus()!= ElectionStatus.DRAFT){
            throw new BusinessException(ElectionErrorCode.ELECTION_IS_NOT_EDITABLE);
        }

        return CsvTemplateGenerator.voterTemplate();
    }
}
