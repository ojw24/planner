package com.ojw.planner.core.enumeration.system.storage;

import com.ojw.planner.core.enumeration.converter.EnumConverter;
import com.ojw.planner.core.enumeration.mapper.EnumMapper;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageType implements EnumMapper {

    LOCAL("storage_type_001", "server local"),
    NAS("storage_type_002","nas"),
    S3("storage_type_003","s3(compatible storage included)");

    private final String code;

    private final String description;

    @Converter(autoApply = true)
    static class StorageTypeConverter extends EnumConverter<StorageType> {
        public StorageTypeConverter() {
            super(StorageType.class);
        }
    }

}
