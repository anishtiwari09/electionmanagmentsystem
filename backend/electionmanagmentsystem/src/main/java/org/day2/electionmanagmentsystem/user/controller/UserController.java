package org.day2.electionmanagmentsystem.user.controller;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;
import org.day2.electionmanagmentsystem.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;



    @GetMapping
    public UserProfileResponse getProfile(@RequestParam String email){
        return userService.findByEmail(email);
    }
}
