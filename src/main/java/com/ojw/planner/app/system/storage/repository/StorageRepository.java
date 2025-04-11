package com.ojw.planner.app.system.storage.repository;

import com.ojw.planner.app.system.storage.domain.Storage;
import com.ojw.planner.core.enumeration.system.storage.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {

    Optional<Storage> findFirstByStorageTypeOrderByIdDesc(StorageType type);

}
