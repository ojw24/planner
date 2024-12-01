package com.ojw.planner.core.util;

import com.ojw.planner.core.util.dto.smtp.SMTPRequest;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SMTPUtil {

    private final JavaMailSender mailSender;

    public void send(SMTPRequest request) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getTo()));
            message.setSubject(request.getSubject());
            message.setContent(request.getBody() != null ? request.getBody() : "", "text/html; charset=UTF-8");

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
