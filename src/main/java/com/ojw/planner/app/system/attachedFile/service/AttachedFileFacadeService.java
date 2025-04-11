package com.ojw.planner.app.system.attachedFile.service;

import com.ojw.planner.app.system.attachedFile.domain.dto.AttachedFileCreateDto;
import com.ojw.planner.app.system.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AttachedFileFacadeService {

    private final AttachedFileService fileService;

    private final StorageService storageService;

    /**
     * 첨부파일 업로드
     *
     * @param createDto - 등록 정보
     * @return 생성된 파일 아이디
     */
    @Transactional
    public Long uploadFile(AttachedFileCreateDto createDto) {
        return fileService.createFile(createDto, storageService.getLocalStorage());
    }

}
