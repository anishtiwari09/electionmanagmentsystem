package org.day2.electionmanagmentsystem.user;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.auth.dto.request.LoginRequest;
import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.common.enums.UserStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserProfileResponse findByEmail(String email){
       User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
       return UserProfileResponse.builder().publicId(user.getPublicId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .addressLine1(user.getAddress()).build();
   }
public UserProfileResponse registerUser(RegisterRequest registerRequest){

        User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if(user!=null) throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
         user = new User();
         user.setEmail(registerRequest.getEmail().trim().toLowerCase());
         user.setFirstName(registerRequest.getFirstName().trim().toLowerCase());
         user.setLastName(registerRequest.getLastName().trim().toLowerCase());
         user.setStatus(UserStatus.ACTIVE);
         user = userRepository.save(user);
        return UserProfileResponse.builder().publicId(user.getPublicId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phoneNumber(user.getPhone())
            .addressLine1(user.getAddress()).build();
}


}
