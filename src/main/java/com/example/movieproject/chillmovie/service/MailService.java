package com.example.movieproject.chillmovie.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendMail(String recipients, String subject, String content, MultipartFile[] files) throws MessagingException {
        log.info("Sending Mail....");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        try {
            // Tham số thứ 1 là email, tham số thứ 2 là Tên hiển thị bạn muốn
            helper.setFrom(emailFrom, "Chill Movie System");
        } catch (UnsupportedEncodingException e) {
            helper.setFrom(emailFrom); // Fallback nếu lỗi
        } //TODO gan ten

        if(recipients.contains(",")){
            helper.setTo(InternetAddress.parse(recipients));
        }else {
            helper.setTo(recipients);
        }

        if(files!=null){
            for (MultipartFile file : files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
        log.info("Mail sent successfully, recipients: "+recipients);
        return "sent";

    }

    public void sendConfirmLink(String emailTo, Long userId, String secretCode) throws MessagingException {
        log.info("Sending Confirm Email....");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("http://localhost:8080/confirm/%s?secretCode=%s", userId, secretCode) ;
        Map<String,Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);
        try {
            helper.setFrom(emailFrom, "Chill Movie System");
        } catch (UnsupportedEncodingException e) {
            helper.setFrom(emailFrom);
        }
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your account");

        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        mailSender.send(message);
        log.info("Mail sent successfully, emailTo: "+emailTo);



    }

    @KafkaListener(topics = "confirmAccount",groupId = "confirm-account-group")
    private void sendConfirmLinkByKafka(String message) throws MessagingException {
        log.info("Sending link Confirm Email....");

        String[] arr = message.split(",");
        String emailTo = arr[0];
        Long userId = Long.parseLong(arr[1]);
        String secretCode =  arr[2];
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("http://localhost:8080/confirm/%s?secretCode=%s", userId, secretCode) ;
        Map<String,Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);
        try {
            helper.setFrom(emailFrom, "Chill Movie System");
        } catch (UnsupportedEncodingException e) {
            helper.setFrom(emailFrom);
        }
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your account");

        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);
        mailSender.send(mimeMessage);
        log.info(" Sending mail sent successfully, emailTo: "+emailTo);



    }
}
