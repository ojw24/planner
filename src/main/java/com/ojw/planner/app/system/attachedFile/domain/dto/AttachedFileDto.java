package com.ojw.planner.app.system.attachedFile.domain.dto;

import com.ojw.planner.app.system.attachedFile.domain.AttachedFile;
import com.ojw.planner.core.enumeration.mapper.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachedFileDto {

    @Schema(description = "첨부파일 아이디")
    private Long attcFileId;

    @Schema(description = "스토리지 아이디")
    private Long storageId;

    @Schema(description = "스토리지 타입")
    private EnumValue storageType;

    @Schema(description = "파일 타입")
    private EnumValue fileType;

    @Schema(description = "권한")
    private EnumValue auth;

    @Schema(description = "원본 파일명")
    private String name;

    @Schema(description = "경로")
    private String path;

    @Schema(description = "등록 일시")
    private LocalDateTime regDtm;

    public static AttachedFileDto of(AttachedFile file) {
        return AttachedFileDto.builder()
                .attcFileId(file.getAttcFileId())
                .storageId(file.getStorage().getStorageId())
                .storageType(
                        EnumValue.toEnumValue(
                                file.getStorage().getStorageType().getClass()
                                , file.getStorage().getStorageType().getCode()
                        )
                )
                .fileType(
                        EnumValue.toEnumValue(
                                file.getFileType().getClass()
                                , file.getFileType().getCode()
                        )
                )
                .auth(
                        ObjectUtils.isEmpty(file.getAuth())
                                ? null
                                : EnumValue.toEnumValue(
                                        file.getAuth().getClass()
                                        , file.getAuth().getCode()
                                )
                )
                .name(file.getName())
                .path(file.getPath())
                .regDtm(file.getRegDtm())
                .build();
    }

}
