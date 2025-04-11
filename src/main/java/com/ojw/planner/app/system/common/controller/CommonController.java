package com.ojw.planner.app.system.common.controller;

import com.ojw.planner.app.system.common.service.CommonService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Common", description = "공통 API")
@Validated
@RequestMapping("/common")
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonService commonService;

    @Operation(summary = "MQ 설정 조회", tags = "Common")
    @GetMapping(path = "/mq-config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMqConfig() throws Exception {
        return new ResponseEntity<>(new ApiResponse<>(commonService.getMqConfig()), HttpStatus.OK);
    }

    @Operation(summary = "MQ Queue 삭제", tags = "Common")
    @DeleteMapping(path = "/mq-queue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteQueue(@RequestParam @NotBlank String name) throws Exception {
        commonService.deleteQueue(name);
        return new ResponseEntity<>(new ApiResponse<>("success"), HttpStatus.OK);
    }

}
