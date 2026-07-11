package org.day2.electionmanagmentsystem.electioncandidate.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.common.csv.CandidateCsvParser;
import org.day2.electionmanagmentsystem.common.csv.CandidateTemplateCsvGenerator;
import org.day2.electionmanagmentsystem.common.enums.CsvField;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationError;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.*;
import org.day2.electionmanagmentsystem.common.helper.Normalize;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.election.repo.ElectionRepository;
import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateTemplatePositionRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CreateElectionCandidateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.GenerateCandidateTemplateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.helper.validator.CandidateUploadValidator;
import org.day2.electionmanagmentsystem.electioncandidate.repo.ElectionCandidateRepository;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.position.repo.ElectionPositionRepository;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.repo.UserRepository;
import org.day2.electionmanagmentsystem.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CandidateServiceImpl implements CandidateService{
    private final ElectionRepository electionRepository;
    private final ElectionPositionRepository electionPositionRepository;
    private final ElectionCandidateRepository electionCandidateRepository;
    private final CandidateCsvParser candidateCsvParser;
    private final UserService userService;
    private final UserRepository userRepository;
    private final int MAX_ALLOWED_CANDIDATES=450000;

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

    @Override
    @Transactional
    public void uploadCandidates(UUID electionId, UUID userPublicId, MultipartFile file){
        Election election = electionRepository.findByPublicIdAndUserPublicId(electionId,userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        User user = userRepository.findByPublicId(userPublicId).orElseThrow(()-> new BusinessException(ErrorCode.UNAUTHORIZED));
        if(election.getStatus()!=ElectionStatus.DRAFT){
            throw new BusinessException(ElectionErrorCode.ELECTION_IS_NOT_EDITABLE);
        }
        boolean candidateAlreadyUploaded=electionCandidateRepository.existsByPositionElection(election);

        if(candidateAlreadyUploaded){
            throw new BusinessException(CandidateErrorCode.CANDIDATES_ALREADY_EXIST);
        }
        List<ElectionPosition> electionPositions = electionPositionRepository.findByElection(election);
        if(electionPositions.isEmpty()){
            throw new BusinessException(PositionErrorCode.NO_POSITION_FOUND);
        }
        List <CandidateCsvRow> rows=candidateCsvParser.parse(file);

        rows = CandidateUploadValidator.cleanRows(rows);

        validatePosition(rows,electionPositions);

        List<RegisterRequest> registerRequests = rows.stream().map(this::createRegisterRequest).toList();

         Map<String,User> userMap=userService.getOrCreateUsers(registerRequests);
        createCandidates(rows,electionPositions,userMap,user);



    }
    private void validatePosition(List<CandidateCsvRow> rows, List<ElectionPosition> electionPositions){
Map<UUID,CandidateCsvRow> csvPositionMap=rows.stream().collect(Collectors.toMap(CandidateCsvRow::getPositionId,Function.identity(),(existing,duplicate)->existing));
    List<CsvValidationError> validationErrors = new ArrayList<>();
for(ElectionPosition position: electionPositions)

    if(csvPositionMap.get(position.getPublicId())==null){
      validationErrors.add(createError(position.getPublicId(),position.getName(),"Missing Required position id", CsvField.MISSING_POSITION_ID));
    }
    Map<UUID,ElectionPosition> electionPositionMap = electionPositions.stream().collect(Collectors.toMap(ElectionPosition::getPublicId,Function.identity()));

    for(CandidateCsvRow row : csvPositionMap.values()){
        if(electionPositionMap.get(row.getPositionId())==null){
            validationErrors.add(createError(row.getPositionId(),row.getPositionName(),"Invalid position id",CsvField.INVALID_POSITION_ID));
        }
    }
if(!validationErrors.isEmpty()){
    throw new CsvValidationException(validationErrors);
}
}
    private CsvValidationError createError( UUID positionId, String positionName, String message, CsvField filed){
        return CsvValidationError.builder()
                .field(filed)
                .row(null)
                .message(message)
                .positionId(positionId)
                .positionName(positionName)
                .build();
    }
    private RegisterRequest createRegisterRequest(CandidateCsvRow row){
        return RegisterRequest.builder()
                .email(row.getEmail())
                .firstName(row.getFirstName())
                .lastName(row.getLastName())
                .build();
    }

    private void createCandidates(List<CandidateCsvRow> rows, List<ElectionPosition> electionPositions, Map<String,User> userMap,User user){
        Map<UUID, ElectionPosition> positionMap =
                electionPositions.stream()
                        .collect(Collectors.toMap(
                                ElectionPosition::getPublicId,
                                Function.identity()
                        ));
        List<ElectionCandidate> electionCandidates =new ArrayList<>();
        for(CandidateCsvRow row : rows){
            ElectionCandidate candidate=new ElectionCandidate();
            candidate.setPosition(positionMap.get(row.getPositionId()));
            candidate.setUser(userMap.get(Normalize.normalizeEmail(row.getEmail())));
            candidate.setAddedBy(user);
            electionCandidates.add(candidate);
        }
        electionCandidateRepository.saveAll(electionCandidates);
    }

}
