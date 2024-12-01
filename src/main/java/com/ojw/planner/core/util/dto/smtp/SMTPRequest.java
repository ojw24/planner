package com.ojw.planner.core.util.dto.smtp;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SMTPRequest {

    private String to;

    private String subject;

    private String body;

    //TODO : UI 작업 시 템플릿 작업
    public SMTPRequest passwordReset(String userId) {
        this.body = "";
        return this;
    }

}
