package org.day2.electionmanagmentsystem.auth.controller;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.auth.dto.request.LoginRequest;
import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.UserService;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    final private UserService userService;
    @PostMapping("/login")
    public UserProfileResponse getUser(@RequestBody LoginRequest loginRequest){


        return  userService.findByEmail(loginRequest.getEmail());
    }
    @PostMapping("/register")
    public final UserProfileResponse register(@RequestBody RegisterRequest registerRequest){
        return userService.registerUser(registerRequest);
    }
}
