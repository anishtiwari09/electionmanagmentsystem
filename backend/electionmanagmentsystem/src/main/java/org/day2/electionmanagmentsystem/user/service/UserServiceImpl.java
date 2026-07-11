package org.day2.electionmanagmentsystem.user.service;

import lombok.RequiredArgsConstructor;
import org.day2.electionmanagmentsystem.auth.dto.request.RegisterRequest;
import org.day2.electionmanagmentsystem.common.enums.UserStatus;
import org.day2.electionmanagmentsystem.common.exception.BusinessException;
import org.day2.electionmanagmentsystem.common.exception.CsvValidationError;
import org.day2.electionmanagmentsystem.common.exception.ErrorCode.ErrorCode;
import org.day2.electionmanagmentsystem.common.helper.Normalize;
import org.day2.electionmanagmentsystem.user.User;
import org.day2.electionmanagmentsystem.user.dto.response.UserProfileResponse;
import org.day2.electionmanagmentsystem.user.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    @Override
    public UserProfileResponse findByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserProfileResponse.builder().userId(user.getPublicId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .addressLine1(user.getAddress()).build();
    }

    @Override
    public UserProfileResponse registerUser(RegisterRequest registerRequest){

        User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if(user!=null) throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
        user = new User();
        user.setEmail(registerRequest.getEmail().trim().toLowerCase());
        user.setFirstName(registerRequest.getFirstName().trim().toLowerCase());
        user.setLastName(registerRequest.getLastName().trim().toLowerCase());
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        return UserProfileResponse.builder().userId(user.getPublicId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .addressLine1(user.getAddress()).build();
    }
    @Override
    public Map<String, User> getOrCreateUsers(List<RegisterRequest> registerRequests){

        Set<String> emails = registerRequests.stream().map(row-> Normalize.normalizeEmail(row.getEmail())).collect(Collectors.toSet());
        List<User> existingUsers = userRepository.findByEmailIn(emails);
        Map<String,User> userMap = existingUsers.stream().collect(Collectors.toMap(user -> Normalize.normalizeEmail(user.getEmail()), Function.identity()));
        List<User> userToCreate = new ArrayList<>();
        for(RegisterRequest registerRequest: registerRequests){
            String email = Normalize.normalizeEmail(registerRequest.getEmail());
            User existingUser = userMap.get(email);
            if(existingUser==null){
               User user = new User();
               user.setFirstName(registerRequest.getFirstName());
               user.setLastName(registerRequest.getLastName());
               user.setEmail(email);
               userMap.put(email,user);
               userToCreate.add(user);
               user.setStatus(UserStatus.ACTIVE);
            }
        }

        if(!userToCreate.isEmpty()){
            userRepository.saveAll(userToCreate);
        }

        return userMap;

    }

}
