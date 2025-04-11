package com.ojw.planner.app.system.attachedFile.domain.dto;

import com.ojw.planner.app.system.attachedFile.domain.AttachedFile;
import com.ojw.planner.app.system.storage.domain.Storage;
import com.ojw.planner.core.enumeration.system.attachedFile.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "첨부파일 등록 DTO")
public class AttachedFileCreateDto {
    
    //TODO : 추후 S3 스토리지 추가될 시, 권한 정보 받기

    //TODO : 추후 파일 타입 추가될 시, 파일 타입 정보 받기

    @NotBlank
    @Schema(description = "원본 파일명")
    private String name;

    @NotNull
    @Schema(description = "파일")
    private MultipartFile file;

    public AttachedFile toEntity(Storage storage, String path) {
        return AttachedFile.builder()
                .storage(storage)
                .fileType(FileType.IMAGE)
                .name(this.name)
                .path(path)
                .build();
    }

}
