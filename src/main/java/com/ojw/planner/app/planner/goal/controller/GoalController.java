package com.ojw.planner.app.planner.goal.controller;

import com.ojw.planner.app.planner.goal.domain.dto.GoalCreateDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalFindDto;
import com.ojw.planner.app.planner.goal.domain.dto.GoalUpdateDto;
import com.ojw.planner.app.planner.goal.service.GoalFacadeService;
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

@Tag(name = "Goal", description = "목표 API")
@Validated
@RequestMapping("/goal")
@RequiredArgsConstructor
@RestController
public class GoalController {

    private final GoalFacadeService goalFacadeService;

    @Operation(summary = "목표 등록", tags = "Goal")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGoal(@RequestBody @Valid GoalCreateDto createDto) {
        return new ResponseEntity<>(
                new ApiResponse<>("Goal creation successful", goalFacadeService.createGoal(createDto))
                , HttpStatus.CREATED
        );
    }

    @Operation(summary = "목표 조회", tags = "Goal")
    @GetMapping(path = "/{goalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGoal(
            @Parameter(name = "goalId", required = true) @NotNull @Positive
            @PathVariable("goalId") Long goalId
            , @Parameter(name = "detail", description = "상세 조회 여부", example = "false", required = true)
            @RequestParam(value = "detail") boolean detail
    ) {
        return new ResponseEntity<>(new ApiResponse<>(goalFacadeService.findGoal(goalId, detail)), HttpStatus.OK);
    }

    @Operation(summary = "목표 조회(조건)", tags = "Goal")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGoalBy(@ParameterObject @Valid GoalFindDto findDto) {
        return new ResponseEntity<>(new ApiResponse<>(goalFacadeService.findGoalBy(findDto)), HttpStatus.OK);
    }

    @Operation(summary = "목표 수정", tags = "Goal")
    @PutMapping(path = "/{goalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGoal(
            @Parameter(name = "goalId", required = true) @NotNull @Positive
            @PathVariable("goalId") Long goalId
            , @RequestBody @Valid GoalUpdateDto updateDto
    ) {
        goalFacadeService.updateGoal(goalId, updateDto);
        return new ResponseEntity<>(new ApiResponse<>("Goal update successful"), HttpStatus.OK);
    }

    @Operation(summary = "목표 삭제", tags = "Goal")
    @DeleteMapping(path = "/{goalId}")
    public ResponseEntity<?> deleteGoal(
            @Parameter(name = "goalId", required = true) @NotNull @Positive
            @PathVariable("goalId") Long goalId
    ) {
        goalFacadeService.deleteGoal(goalId);
        return new ResponseEntity<>(new ApiResponse<>("Goal delete successful"), HttpStatus.OK);
    }

}
