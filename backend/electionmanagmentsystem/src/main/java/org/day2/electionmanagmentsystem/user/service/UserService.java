package org.day2.electionmanagmentsystem.user.service;

import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.electioncandidate.dto.request.CandidateCsvRow;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    public UserProfileResponse findByEmail(String email);
    public UserProfileResponse registerUser(RegisterRequest registerRequest);
    public Map<String, User> getOrCreateUsers(List<RegisterRequest> rows);

}
