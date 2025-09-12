package org.workingproject0505.service.util;

import org.springframework.stereotype.Component;
import org.workingproject0505.dto.UserRequestDto;
import org.workingproject0505.dto.UserResponseDto;
import org.workingproject0505.entity.User;

import java.util.List;

@Component
public class UserConverter {

    public User fromDto(UserRequestDto dto) {
        User user = new User();

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public UserResponseDto toDto(User user){
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }


    public List<UserResponseDto> toDtos(List<User> users){
        return users.stream()
                .map(user -> toDto(user))
                .toList();
    }

}
