package org.day2.electionmanagmentsystem.position.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionDetailsResponse;
import org.day2.electionmanagmentsystem.position.dto.request.CreatePositonsRequest;
import org.day2.electionmanagmentsystem.position.service.ElectionPositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ElectionPositionController {
    final ElectionPositionService electionPositionService;
    @PostMapping("/elections/{electionId}/positions")
    public ResponseEntity<ApiResponse<Void>> createElectionPositions(@PathVariable UUID electionId, @Valid @RequestBody CreatePositonsRequest requests, @RequestHeader("x-userId") UUID userId){
        electionPositionService.createPositions(electionId,userId,requests);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
    }
}
