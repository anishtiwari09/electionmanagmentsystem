package org.day2.electionmanagmentsystem.election.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionDetailsResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionsResponse;
import org.day2.electionmanagmentsystem.election.service.ElectionService;
import org.day2.electionmanagmentsystem.electioncandidate.service.CandidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ElectionController {
  private final ElectionService electionService;
    private final CandidateService candidateService;
  @GetMapping("/elections")
   public ResponseEntity<ApiResponse<ElectionsResponse>> getElection(@ModelAttribute GetElectionsRequest electionsRequest,  @RequestHeader("x-userId") UUID userId){

      ElectionsResponse electionsResponse= electionService.getElections(userId,electionsRequest);

      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(electionsResponse));
  }
  @PostMapping("/elections")
    public ResponseEntity<ApiResponse<ElectionResponse>> createElection(@Valid  @RequestBody CreateElectionRequest createElectionRequest, @RequestHeader("x-userId") UUID userId){

       ElectionResponse electionResponse= electionService.createNewElection(userId,createElectionRequest);

     return  ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(electionResponse));
  }
  @GetMapping("/elections/{electionId}")
    public ResponseEntity<ApiResponse<ElectionDetailsResponse>> getElectionDetails(@PathVariable UUID electionId, @RequestHeader("x-userId") UUID userId){
      ElectionDetailsResponse response = electionService.getElectionDetails(electionId, userId);
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
  }

  @PostMapping("/elections/{electionId}/candidates/upload")
    public ResponseEntity<ApiResponse<Void>> bulkUploadCandidates(@PathVariable UUID electionId, @RequestParam("file") MultipartFile file,  @RequestHeader("x-userId") UUID userId){
      candidateService.uploadCandidates(electionId,userId,file) ;
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
  }
}
