package com.example.movieproject.chillmovie.controller;


import com.example.movieproject.chillmovie.entity.Actor;
import com.example.movieproject.chillmovie.entity.RestResponse;
import com.example.movieproject.chillmovie.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/common")
@Tag(name = "Common Controller")
@RequiredArgsConstructor
public class CommonController {

    private final MailService mailService;

    @PostMapping(path = "/send-email", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendEmail(@RequestParam String recipient, @RequestParam String subject, @RequestParam String content, @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(mailService.sendMail(recipient, subject, content, files));
        }catch (Exception me){
            log.error("Send email failed, errorMessage={}", me.getMessage());
            return new ResponseEntity<>("Send email failed, errorMessage=", HttpStatus.BAD_REQUEST);
        }


    }

}
