package org.day2.electionmanagmentsystem.electioncandidate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.GenerateCandidateTemplateRequest;
import org.day2.electionmanagmentsystem.electioncandidate.service.CandidateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {
    final private CandidateService candidateService;
    @PostMapping("/generate-template")

    public ResponseEntity<byte[]> generateCandidateTemplate(@RequestHeader("x-userId") UUID userId,   @Valid @RequestBody GenerateCandidateTemplateRequest request){
        byte[] csv = candidateService.generateTemplate(
                userId,
                request
        );
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=candidate-template.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
