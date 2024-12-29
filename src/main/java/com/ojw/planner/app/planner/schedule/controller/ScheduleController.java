package com.ojw.planner.app.planner.schedule.controller;

import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleCreateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleFindDto;
import com.ojw.planner.app.planner.schedule.domain.dto.ScheduleUpdateDto;
import com.ojw.planner.app.planner.schedule.domain.dto.request.ScheduleShareRequestCreateDto;
import com.ojw.planner.app.planner.schedule.service.ScheduleFacadeService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule", description = "일정 API")
@Validated
@RequestMapping("/schedule")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleFacadeService scheduleFacadeService;

    @Operation(summary = "일정 등록", tags = "Schedule")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSchedule(@RequestBody @Valid ScheduleCreateDto createDto) {
        return new ResponseEntity<>(
                new ApiResponse<>("Schedule creation successful", scheduleFacadeService.createSchedule(createDto))
                , HttpStatus.CREATED
        );
    }

    @Operation(summary = "일정 조회", tags = "Schedule")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findSchedules(@ParameterObject @Valid ScheduleFindDto findDto) {
        return new ResponseEntity<>(new ApiResponse<>(scheduleFacadeService.findSchedules(findDto)), HttpStatus.OK);
    }

    @Operation(summary = "일정 수정", tags = "Schedule")
    @PutMapping(path = "/{scheduleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSchedule(
            @Parameter(name = "scheduleId", required = true) @NotNull @Positive
            @PathVariable("scheduleId") Long scheduleId
            , @RequestBody @Valid ScheduleUpdateDto updateDto
    ) {
        scheduleFacadeService.updateSchedule(scheduleId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Schedule update successful"), HttpStatus.OK);
    }

    @Operation(summary = "일정 삭제", tags = "Schedule")
    @DeleteMapping(path = "/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(
            @Parameter(name = "scheduleId", required = true) @NotNull @Positive
            @PathVariable("scheduleId") Long scheduleId
    ) {
        scheduleFacadeService.deleteSchedule(scheduleId);
        return new ResponseEntity<>(new ApiResponse<>("Schedule delete successful"), HttpStatus.OK);
    }

    @Operation(summary = "일정 공유 신청 등록", tags = "Schedule")
    @PostMapping(path = "/{scheduleId}/share", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createScheduleShareRequest(
            @Parameter(name = "scheduleId", required = true) @NotNull @Positive
            @PathVariable("scheduleId") Long scheduleId
            , @RequestBody @Valid ScheduleShareRequestCreateDto createDto
    ) {
        scheduleFacadeService.createScheduleShareRequests(scheduleId, createDto);
        return new ResponseEntity<>(new ApiResponse<>("Schedule share request creation successful"), HttpStatus.CREATED);
    }

    @Operation(summary = "일정 공유 신청 승인", tags = "Schedule")
    @PutMapping(path = "/{scheduleId}/share/{reqId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> approveScheduleShareRequest(
            @Parameter(name = "scheduleId", required = true) @NotNull @Positive
            @PathVariable("scheduleId") Long scheduleId
            , @Parameter(name = "reqId", required = true) @NotNull @Positive
            @PathVariable("reqId") Long reqId
            , @Parameter(name = "approve", description = "승인 여부", example = "false", required = true)
            @RequestParam(value = "approve") boolean approve
    ) {
        scheduleFacadeService.approveScheduleShareRequest(reqId, approve);
        return new ResponseEntity<>(new ApiResponse<>("Schedule share request approve successful"), HttpStatus.OK);
    }

}
