package com.ojw.planner.app.system.attachedFile.service;

import com.ojw.planner.app.system.attachedFile.domain.dto.AttachedFileCreateDto;
import com.ojw.planner.app.system.attachedFile.repository.AttachedFileRepository;
import com.ojw.planner.app.system.storage.domain.Storage;
import com.ojw.planner.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AttachedFileService {

    private final AttachedFileRepository fileRepository;

    @Transactional
    public Long createFile(AttachedFileCreateDto createDto, Storage storage) {
        return fileRepository.save(createDto.toEntity(storage, upload(createDto.getFile(), storage)))
                .getAttcFileId();
    }

    private String upload(MultipartFile file, Storage storage) {

        String path = getPath(file, storage);

        try {
            file.transferTo(new File(path));
        } catch(Exception e) {
            throw new ResponseException("error occurred while file upload..", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return path;

    }

    private String getPath(MultipartFile file, Storage storage) {
        return System.getProperty("user.home")
                + storage.getPath()
                + File.separator
                + UUID.randomUUID()
                + "."
                + FilenameUtils.getExtension(file.getOriginalFilename());
    }

}
