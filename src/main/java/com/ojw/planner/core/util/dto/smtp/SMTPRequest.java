package com.ojw.planner.core.util.dto.smtp;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SMTPRequest {

    private String to;

    private String subject;

    private String body;

    public SMTPRequest findId(String userId, String home) {
        this.body = "[Planner]<br />" +
                "요청하신 아이디 찾기 결과입니다.<br />" +
                "아이디 : " + userId + "<br />" +
                "<br />" +
                "<a href=\"" + home + "\">로그인하기</a><br />" +
                "문의사항은 회신 부탁드립니다. 감사합니다.";

        return this;
    }

    public SMTPRequest passwordReset(String userId, String url, String key, Long passwordExpire) {
        this.body = "[Planner]<br />" +
                "아래 링크를 눌러 비밀번호를 재설정해주세요.<br />" +
                "<a href=\"" + url +
                "?userId=" + userId +
                "&key=" + key +
                "\">비밀번호 재설정</a><br />" +
                "<br />" +
                getExpire(passwordExpire) +
                "문의사항은 회신 부탁드립니다. 감사합니다.";

        return this;
    }

    private String getExpire(Long expire) {
        return expire != null
                ? "해당 링크는 " + dateToString(expire) + "까지 유효합니다.<br />"
                : "";
    }

    private String dateToString(Long expire) {
        return LocalDateTime.now()
                .plusSeconds(expire / 1000)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
