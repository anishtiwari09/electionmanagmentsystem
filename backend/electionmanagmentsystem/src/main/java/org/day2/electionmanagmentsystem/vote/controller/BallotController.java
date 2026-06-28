package org.day2.electionmanagmentsystem.vote.controller;
import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.common.dto.response.ApiResponse;
import org.day2.electionmanagmentsystem.service.BallotService;
import org.day2.electionmanagmentsystem.vote.dto.response.BallotResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class BallotController {
    final private BallotService ballotService;

    @GetMapping("/api/v1/elections/{electionPublicId}/ballot")

    public ApiResponse<BallotResponse> getBallot(@PathVariable UUID electionPublicId, @RequestHeader("X-USER-ID") UUID userPublicId){
        BallotResponse ballotResponse = ballotService.getBallot(electionPublicId,userPublicId);

        return ApiResponse.<BallotResponse>builder()
                .success(true)
                .message("Ballot fetched successfully")
                .data(ballotResponse)
                .build();
    }

}
