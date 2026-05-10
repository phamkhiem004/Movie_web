package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.DTO.CreateUserRequest;
import com.example.movieproject.chillmovie.DTO.LoginDTO;
import com.example.movieproject.chillmovie.DTO.RegisterDTO;
import com.example.movieproject.chillmovie.DTO.ResLoginDTO;
import com.example.movieproject.chillmovie.entity.RestResponse;
import com.example.movieproject.chillmovie.entity.User;
import com.example.movieproject.chillmovie.entity.UserStatus;
import com.example.movieproject.chillmovie.service.UserService;
import com.example.movieproject.chillmovie.util.SecurityUtil;
import com.example.movieproject.config.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Auth Controller")
@RestController
public class AuthController {


    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @Operation(summary = "Login", description = "API login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {

        // 1. Lấy thông tin user từ DB để kiểm tra trạng thái trước
        // (Bạn có thể viết thêm hàm findByUsername trong UserService nhé)
        User user = userService.getUserByUserName(loginDTO.getUsername());

        // Nếu user không tồn tại, để cho Spring Security tự lo ở bước authenticate bên dưới,
        // hoặc bạn có thể báo lỗi luôn ở đây.
        if (user != null) {
            if (user.getStatus() == UserStatus.INACTIVE) {
                // Trả về lỗi 403 Forbidden hoặc 400 Bad Request tùy bạn
                RestResponse<String> res = new RestResponse<>();
                res.setStatusCode(403);
                res.setMessage("Tài khoản chưa được xác thực. Vui lòng kiểm tra email của bạn!");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
            }

            if (user.getStatus() == UserStatus.BLOCKED) {
                RestResponse<String> res = new RestResponse<>();
                res.setStatusCode(403);
                res.setMessage("Tài khoản của bạn đã bị khóa!");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
            }
        }

        // 2. Nếu status là ACTIVE, tiếp tục luồng xác thực bình thường của bạn
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            // Xác thực người dùng (check password)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // Tạo token và trả về
            String access_token = this.securityUtil.createToken(authentication);
            ResLoginDTO resLoginDTO = new ResLoginDTO();
            resLoginDTO.setToken(access_token);

            return ResponseEntity.ok().body(resLoginDTO);

        } catch (Exception e) {
            // Bắt lỗi sai mật khẩu hoặc tài khoản không tồn tại
            RestResponse<String> res = new RestResponse<>();
            res.setStatusCode(401);
            res.setMessage("Sai tài khoản hoặc mật khẩu");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    @Operation(summary = "Register", description = "API register")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest user) throws MessagingException {
        User u = userService.getUserByUserName(user.getUsername());

        if (u != null) {
            RestResponse<String> res = new RestResponse<>();
            res.setStatusCode(403);
            res.setMessage("Tên tài khoản đã tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);}
        RegisterDTO dto = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

    @GetMapping("/confirm/{userId}")
    public ResponseEntity<?> confirmUser(@PathVariable @Min(1) String userId, @RequestParam String secretCode) {
        log.info("Confirm User {}, secretCode ={} ", userId, secretCode);
        try{
            userService.confirmUser(userId, secretCode);
            RestResponse<Boolean> res = new RestResponse<>();
            res.setStatusCode(200);
            res.setMessage("User successfully confirmed");


            return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);

        }catch (Exception e){
            log.error("error confirming user {}", e.getMessage(), e.getCause());
            RestResponse<Boolean> res = new RestResponse<>();
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

        }finally {
            //direct to login page
        }
    }
}
