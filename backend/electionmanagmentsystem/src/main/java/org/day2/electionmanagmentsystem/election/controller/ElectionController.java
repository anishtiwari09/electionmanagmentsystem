package org.day2.electionmanagmentsystem.election.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.election.dto.request.CreateElectionRequest;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionsResponse;
import org.day2.electionmanagmentsystem.election.service.ElectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ElectionController {
  final ElectionService electionService;

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
}
