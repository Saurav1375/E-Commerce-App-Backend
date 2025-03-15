package org.example.ecommerce.service;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.UserDto;
import org.example.ecommerce.mappers.UserMapper;
import org.example.ecommerce.model.user.User;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public UserDto getUserProfile(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toUserDto).orElse(null);
    }

    public boolean updateUserProfile(String username, UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setFirstname(userDto.getFirstname());
            updatedUser.setLastname(userDto.getLastname());
            updatedUser.setBirthdate(userDto.getBirthdate());
            saveUser(updatedUser);
            return true;
        }
        return false;
    }
}
