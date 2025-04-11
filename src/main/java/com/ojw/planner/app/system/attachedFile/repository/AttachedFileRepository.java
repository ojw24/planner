package com.ojw.planner.app.system.attachedFile.repository;

import com.ojw.planner.app.system.attachedFile.domain.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {
}
