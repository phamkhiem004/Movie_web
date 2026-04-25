package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.UpdateUserRequest;
import com.example.movieproject.chillmovie.DTO.UserDTO;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.respository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(map -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(map.getId());
            userDTO.setUsername(map.getUsername());
            userDTO.setEmail(map.getEmail());
            userDTO.setStatus(map.getStatus());
            userDTO.setRole(map.getRoleId());
            userDTO.setCreatedAt(map.getCreatedAt());
            return userDTO;
        }).toList();
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.username);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setFullName(request.fullName);
        user.setCreatedAt(request.createdAt);


        user.setStatus(UserStatus.ACTIVE);
        user.setRoleId(2L);

        User savedUser = userRepository.save(user);

        UserDTO dto = new UserDTO();
        dto.setUsername(savedUser.getUsername());
        dto.setEmail(savedUser.getEmail());
        dto.setStatus(savedUser.getStatus());
        dto.setFullname(savedUser.getFullName());

        return dto;
    }

    public User getUserByID(Long id) {
        Optional<User> movieOptional = userRepository.findById(id);
        return movieOptional.orElse(null);
    }

    @Transactional
    public UserDTO updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != status) {
            user.setStatus(status);
        }

        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRoleId());
        dto.setFullname(user.getFullName());

        return dto;
    }




}
