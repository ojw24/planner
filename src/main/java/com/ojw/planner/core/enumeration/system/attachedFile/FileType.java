package com.ojw.planner.core.enumeration.system.attachedFile;

import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType implements EnumMapper {

    IMAGE("file_type_001", "image");

    private final String code;

    private final String description;

    @Converter(autoApply = true)
    static class FileTypeConverter extends EnumConverter<FileType> {
        public FileTypeConverter() {
            super(FileType.class);
        }
    }

}
