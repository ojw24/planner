package com.ojw.planner.core.util;

import com.ojw.planner.core.util.dto.smtp.SMTPRequest;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SMTPUtil {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    public void send(SMTPRequest request) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from, "Planner");

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getTo()));
            message.setSubject(request.getSubject());
            message.setContent(request.getBody() != null ? request.getBody() : "", "text/html; charset=UTF-8");

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
