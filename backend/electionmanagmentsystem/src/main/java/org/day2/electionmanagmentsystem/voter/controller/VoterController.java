package org.day2.electionmanagmentsystem.voter.controller;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.voter.dto.request.GenerateVoterTemplateRequest;
import org.day2.electionmanagmentsystem.voter.service.VoterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/voters")
@RequiredArgsConstructor
public class VoterController {
    private final VoterService voterService;
    @PostMapping("/generate-template")
    public ResponseEntity<byte[]> generateVoterTemplate(@RequestBody GenerateVoterTemplateRequest generateVoterTemplateRequest, @RequestHeader("x-userId") UUID userId){
        byte[] csv= voterService.generateTemplate(generateVoterTemplateRequest.getElectionId(),userId);
        return ResponseEntity.status(HttpStatus.OK).header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=candidate-template.csv"
        )
                .contentType(MediaType.parseMediaType("text/csv")).body(csv);
    }
}
