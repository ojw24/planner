package com.ojw.planner.app.system.storage.service;

import com.ojw.planner.app.system.storage.domain.Storage;
import com.ojw.planner.app.system.storage.repository.StorageRepository;
import com.ojw.planner.core.enumeration.system.storage.StorageType;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StorageService {

    private final StorageRepository storageRepository;

    //TODO : 현재는 로컬 고정, 추후 스토리지 추가 시 수정
    public Storage getLocalStorage() {
        return storageRepository.findFirstByStorageTypeOrderByIdDesc(StorageType.LOCAL)
                .orElseThrow(() -> new ResponseException("not exist storage", HttpStatus.NOT_FOUND));
    }

}
