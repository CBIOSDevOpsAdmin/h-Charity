package com.himanism.hcharityapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.himanism.hcharityapi.entities.EntityFeedback;

@Repository
public interface EntityFeedbackRepo extends JpaRepository<EntityFeedback, Long> {
}
