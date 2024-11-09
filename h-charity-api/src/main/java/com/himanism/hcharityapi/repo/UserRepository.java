package com.himanism.hcharityapi.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.models.Erole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  List<User> findByRolesNotContaining(Role adminRole);

  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
  List<User> findAllByRoleName(@Param("roleName") Erole roleName);

  @Modifying
  @Transactional
  @Query("DELETE FROM User u WHERE u.id IN :userIds")
  void deleteUsersByIds(List<Long> userIds);
}
