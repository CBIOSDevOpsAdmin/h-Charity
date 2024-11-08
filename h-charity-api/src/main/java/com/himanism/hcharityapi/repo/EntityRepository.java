package com.himanism.hcharityapi.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.himanism.hcharityapi.entities.Entities;

@Repository
public interface EntityRepository extends JpaRepository<Entities, Long> {
    Optional<Entities> findByUserId(Long entityOwnerId);

    List<Entities> findByIsVerifiedTrueOrUserId(Long userId);
}
