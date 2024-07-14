package com.himanism.hcharityapi.repo;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.himanism.hcharityapi.entities.EntityPhotos;

@Repository
public interface EntityPhotosRepository extends JpaRepository<EntityPhotos, Long> {
    Optional<List<EntityPhotos>> findByEntityId(Long entityId);

    void deleteByEntityIdAndIsCoverPhoto(Long entityId, Boolean isCoverPhoto);
    void deleteByEntityIdAndIsQRCode(Long entityId, Boolean isQRCode);
}
