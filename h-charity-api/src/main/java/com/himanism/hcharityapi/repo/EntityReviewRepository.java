package com.himanism.hcharityapi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.himanism.hcharityapi.entities.EntityReview;

@Repository
public interface EntityReviewRepository extends JpaRepository<EntityReview, Long> {
    List<EntityReview> findByEntityId(Long entityId);
}
