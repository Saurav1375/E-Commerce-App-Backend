package org.example.ecommerce.mappers;


import org.example.ecommerce.dto.UserDto;
import org.example.ecommerce.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .build();
    }
}
