package org.day2.electionmanagmentsystem.voter.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.common.csv.CsvTemplateGenerator;
import org.day2.electionmanagmentsystem.common.csv.VoterCsvParser;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ElectionErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.VoterErrorCode;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.repo.UserRepository;
import org.day2.electionmanagmentsystem.user.service.UserService;
import org.day2.electionmanagmentsystem.voter.Voter;
import org.day2.electionmanagmentsystem.voter.dto.request.VoterCsvRow;
import org.day2.electionmanagmentsystem.voter.dto.response.VoterElectionsResponses;
import org.day2.electionmanagmentsystem.voter.helper.validator.VoterUploadValidator;
import org.day2.electionmanagmentsystem.voter.mapper.VoterElectionMapper;
import org.day2.electionmanagmentsystem.voter.repo.VoterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoterServiceImpl implements VoterService{
    final private int MAX_VOTER=1000;
    final private VoterRepository voterRepository;
    final private ElectionRepository electionRepository;
    final private UserRepository userRepository;
    final private VoterCsvParser voterCsvParser;
    final private UserService userService;
    final private VoterElectionMapper voterElectionMapper;
    @Override
    public byte[] generateTemplate(UUID electionId, UUID userId){
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        if(election.getStatus()!= ElectionStatus.DRAFT){
            throw new BusinessException(ElectionErrorCode.ELECTION_IS_NOT_EDITABLE);
        }

        return CsvTemplateGenerator.voterTemplate();

    }
    @Transactional
    @Override
    public void uploadVoters(UUID electionId, UUID userId, MultipartFile file) {
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        User user =userRepository.findByPublicId(userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        if(election.getStatus()!= ElectionStatus.DRAFT){
            throw new BusinessException(ElectionErrorCode.ELECTION_IS_NOT_EDITABLE);
        }
        boolean voterAlreadyUploaded = voterRepository.existsByElection(election);

        if(voterAlreadyUploaded){
            throw new BusinessException(VoterErrorCode.NON_EMPTY_VOTER_LIST);
        }
        List <VoterCsvRow> rows=voterCsvParser.parse(file);
        rows= VoterUploadValidator.cleanRows(rows);
        Map<String, User> voterMap =userService.getOrCreateUsers(
            rows.stream().map(this::createRegisterRequest).toList()
        );
        createVoters(user,election,new ArrayList<>(voterMap.values()));
    }

    @Override
    public VoterElectionsResponses getElections(UUID userId) {
        User user=userRepository.findByPublicId(userId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        List<Voter> voters=voterRepository.findByUser(user);
        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );
        if(voters.isEmpty()){
            throw new BusinessException(VoterErrorCode.NO_ELECTION_FOUND);
        }

        Page<Election> electionPage = electionRepository.findAllByVoter(user,ElectionStatus.ACTIVE,pageable);
       return VoterElectionsResponses
               .builder()
               .page(electionPage.getNumber())
               .size(electionPage.getSize())
               .totalPages(electionPage.getTotalPages())
               .totalElement(electionPage.getTotalElements())
               .elections(
                       electionPage
                               .getContent()
                               .stream().map(voterElectionMapper::toVoterElectionsMapper).toList()
               )
               .build();


    }

    private RegisterRequest createRegisterRequest(VoterCsvRow row){
        return RegisterRequest.builder()
                .email(row.getEmail())
                .firstName(row.getFirstName())
                .lastName(row.getLastName())
                .build();
    }

    private void createVoters(User user, Election election,List<User> voterList){
        List<Voter> voters = new ArrayList<>();
        for(User userVoter: voterList){
            Voter voter= new Voter();
            voter.setUser(userVoter);
            voter.setElection(election);
            voter.setAddedBy(user);
            voters.add(voter);
        }
        voterRepository.saveAll(voters);
    }

}
