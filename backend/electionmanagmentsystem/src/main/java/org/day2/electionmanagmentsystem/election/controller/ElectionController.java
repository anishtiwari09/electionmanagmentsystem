package org.day2.electionmanagmentsystem.election.controller;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.election.dto.request.GetElectionsRequest;
import org.day2.electionmanagmentsystem.election.dto.response.ElectionsResponse;
import org.day2.electionmanagmentsystem.election.service.ElectionService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ElectionController {
  final ElectionService electionService;

  @GetMapping("/elections")
   public ElectionsResponse getElection(@ModelAttribute GetElectionsRequest electionsRequest,  @RequestHeader("x-userId") UUID userId){
      System.out.println(userId);
      return electionService.getElections(userId,electionsRequest);
  }
}
