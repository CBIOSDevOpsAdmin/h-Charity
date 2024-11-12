package com.himanism.hcharityapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.himanism.hcharityapi.entities.EntityFeedbackStatus;

@Repository
public interface EntityFeedbackStatusRepo extends JpaRepository<EntityFeedbackStatus, Long> {
}