package com.example.movieproject.chillmovie.service;

import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.RegisterDTO;
import com.example.movieproject.chillmovie.DTO.UserDTO;
import com.example.movieproject.chillmovie.entity.Movie;
import com.example.movieproject.chillmovie.entity.Role;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.respository.RoleRepository;
import com.example.movieproject.chillmovie.respository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(map -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(map.getId());
            userDTO.setUsername(map.getUsername());
            userDTO.setFullName(map.getFullName());
            userDTO.setEmail(map.getEmail());
            userDTO.setStatus(map.getStatus());
            userDTO.setRole(map.getRole().getRoleName());
            userDTO.setCreatedAt(map.getCreatedAt());
            return userDTO;
        }).toList();
    }

    @Transactional
    public RegisterDTO createUser(CreateUserRequest request) throws MessagingException {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setIsDeleted(false);
        user.setCreatedAt(request.getCreatedAt());


        user.setStatus(UserStatus.INACTIVE);
        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        String generatedSecretCode = UUID.randomUUID().toString();
        user.setSecretCode(generatedSecretCode);

        User savedUser = userRepository.save(user);
        if(user.getId() != null) {
            //Send email confirm here
            mailService.sendConfirmLink(user.getEmail(), user.getId(),generatedSecretCode);
        }

        RegisterDTO dto = new RegisterDTO();
        dto.setUsername(savedUser.getUsername());
        dto.setEmail(savedUser.getEmail());
        dto.setFullName(savedUser.getFullName());

        return dto;
    }

    public UserDTO getUserByID(Long id) {
        User user = userRepository.findById(id).orElse(null);
        UserDTO dto = new UserDTO();
        assert user != null;
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setFullName(user.getFullName());
        dto.setIsDeleted(user.getIsDeleted());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;

    }

    @Transactional
    public UserDTO updateUserStatus(Long id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != status) {
            user.setStatus(status);
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole().getRoleName());
        dto.setFullName(user.getFullName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(Instant.now());


        return dto;
    }


    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);

    }

    @Transactional
    public void confirmUser(@Min(1) String userId, String secretCode) {
        log.info("Confirm User {}, secretCode ={} ", userId, secretCode);
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID này!"));
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản này đã được xác thực trước đó!");
        }
        if (user.getSecretCode() == null || !user.getSecretCode().equals(secretCode)) {
            throw new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn!");
        }
        user.setStatus(UserStatus.ACTIVE);
        user.setSecretCode(null);
        userRepository.save(user);

        log.info("Kích hoạt thành công tài khoản: {}", user.getUsername());

    }
}
