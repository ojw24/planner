package com.ojw.planner.app.system.attachedFile.controller;

import com.ojw.planner.app.system.attachedFile.domain.dto.AttachedFileCreateDto;
import com.ojw.planner.app.system.attachedFile.service.AttachedFileFacadeService;
import com.ojw.planner.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AttachedFile", description = "첨부파일 API")
@Validated
@RequestMapping("/attached-file")
@RequiredArgsConstructor
@RestController
public class AttachedFileController {

    private final AttachedFileFacadeService fileFacadeService;

    @Operation(summary = "첨부파일 업로드", tags = "AttachedFile")
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@Valid @ModelAttribute AttachedFileCreateDto createDto) {
        return new ResponseEntity<>(new ApiResponse<>("file upload success", fileFacadeService.uploadFile(createDto)), HttpStatus.OK);
    }

}
