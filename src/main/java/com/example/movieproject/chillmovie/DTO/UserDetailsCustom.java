package com.example.movieproject.chillmovie.DTO;

import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

    private final UserService userService;

    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        User user = this.userService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username/password không hợp lệ");
        }

        // 1. Tạo đối tượng CustomUserDetails của bạn
        CustomUserDetails customUserDetails =
                new CustomUserDetails();

        // 2. Gán các giá trị từ Entity 'user' (DB) sang 'customUserDetails'
        customUserDetails.setId(user.getId());
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setPassword(user.getPassword());

        // 3. Gán quyền (Role)
        customUserDetails.setAuthorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())
        ));

        // 4. Trả về đối tượng custom này
        return customUserDetails;
    }
}
