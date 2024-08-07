package com.himanism.hcharityapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.himanism.hcharityapi.entities.Appeal;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {}
