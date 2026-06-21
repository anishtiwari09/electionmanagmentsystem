package org.day2.electionmanagmentsystem.user;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserProfileResponse findByEmail(String email){
       User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
       return UserProfileResponse.builder().publicId(user.getPublicId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .addressLine1(user.getAddress()).build();
   }
}
