package com.himanism.hcharityapi.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.MessageResponseDto;
import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.security.services.UserDetailsServiceImpl;
import com.himanism.hcharityapi.services.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class AdminController {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder encoder;
        private final UserDetailsServiceImpl userDetailsService;
        private final AdminService adminService;

        @PostMapping("")
        public ResponseEntity<?> saveUser(@Valid @RequestBody UserReqDto userReqDto) {
                log.info("Admin Controller: Create Users", userReqDto.getUsername());

                try {
                        if (Boolean.TRUE.equals(userRepository.existsByUsername(userReqDto.getUsername()))) {
                                return ResponseEntity
                                                .badRequest()
                                                .body(new MessageResponseDto("Error: Username is already taken!"));
                        }

                        if (Boolean.TRUE.equals(userRepository.existsByEmail(userReqDto.getEmail()))) {
                                return ResponseEntity
                                                .badRequest()
                                                .body(new MessageResponseDto("Error: Email is already in use!"));
                        }

                        // Create new user's account
                        User user = new User(userReqDto.getUsername(),
                                        userReqDto.getFullname(),
                                        userReqDto.getEmail(),
                                        encoder.encode(userDetailsService.defaultPassword(userReqDto.getUsername(),
                                                        userReqDto.getMobile())),
                                        userReqDto.getMobile());

                        log.info("User created successfully", userReqDto.getUsername());

                        String role = userReqDto.getRole();
                        Set<Role> roles = new HashSet<>();

                        switch (role) {
                                case "ADMIN":
                                        Role adminRole = roleRepository.findByName(Erole.ADMIN)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role is not found."));
                                        roles.add(adminRole);
                                        break;

                                case "ORGANISATION_VOLUNTEER":
                                        Role modRole = roleRepository.findByName(Erole.ORGANISATION_VOLUNTEER)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role is not found."));
                                        roles.add(modRole);
                                        break;

                                case "INSTITUTE_OWNER":
                                        Role insOwnerRole = roleRepository.findByName(Erole.INSTITUTE_OWNER)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role is not found."));
                                        roles.add(insOwnerRole);
                                        break;

                                default:
                                        Role userRole = roleRepository.findByName(Erole.NORMAL_USER)
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Error: Role is not found."));
                                        roles.add(userRole);
                        }

                        user.setRoles(roles);
                        userRepository.save(user);

                        log.info("User created successfully", userReqDto.getUsername());
                        return ResponseEntity.ok(new MessageResponseDto("User created successfully!"));

                } catch (RuntimeException e) {
                        log.error("Error: An error occurred while creating the user", userReqDto.getUsername(), e);
                        return ResponseEntity
                                        .status(500)
                                        .body(new MessageResponseDto(
                                                        "Error: Unable to create user. Please try again later."));
                } catch (Exception e) {
                        log.error("Error: Unexpected error occurred during user creation", userReqDto.getUsername(),
                                        e);
                        return ResponseEntity
                                        .status(500)
                                        .body(new MessageResponseDto("Error: Unexpected error occurred."));
                }
        }

        @GetMapping("")
        public ResponseEntity<?> getUsers(Authentication authentication) {
                log.info("Admin Controller: List Users");

                try {
                        List<UserResDto> users = adminService.getUsers(authentication);
                        return ResponseEntity.ok().body(users);
                } catch (RuntimeException e) {
                        log.error("Error occurred while listing users", e);
                        return ResponseEntity
                                        .status(500)
                                        .body("Error: Unable to retrieve users.");
                } catch (Exception e) {
                        log.error("Unexpected error occurred while listing users", e);
                        return ResponseEntity
                                        .status(500)
                                        .body("Error: Unexpected error occurred.");
                }
        }

        @PutMapping("")
        public ResponseEntity<?> updateUser(Authentication authentication, @Valid @RequestBody UserReqDto userReqDto) {
                log.info("Admin Controller: Update Users", userReqDto.getUsername());

                try {
                        UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext()
                                        .getAuthentication()
                                        .getPrincipal();

                        UserResDto userResDto = adminService.updateUser(userReqDto, principle);
                        log.info("User updated successfully", userReqDto.getUsername());

                        return ResponseEntity.ok().body(userResDto);
                } catch (RuntimeException e) {
                        log.error("Error occurred while updating user", userReqDto.getUsername(), e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error: Unable to update user.");
                } catch (Exception e) {
                        log.error("Unexpected error occurred while updating user", userReqDto.getUsername(), e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error: Unexpected error occurred.");
                }
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
                log.info("Admin Controller: delete Users", userId);

                try {
                        adminService.deleteUser(userId);
                        log.info("Successfully deleted user by Id", userId);
                        return ResponseEntity.ok("User deleted successfully.");
                } catch (RuntimeException e) {
                        log.error("Error occurred while deleting user by Id", userId, e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error: Unable to delete user.");
                } catch (Exception e) {
                        log.error("Unexpected error occurred while deleting user by Id", userId, e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Error: Unexpected error occurred.");
                }
        }

        @DeleteMapping("")
        public ResponseEntity<?> deleteUsers(@RequestBody List<Long> userIds) {
                log.info("Admin Controller: delete multiple users", userIds);

                try {
                        adminService.deleteUsersByIds(userIds);
                        log.info("Successfully deleted users with IDs ", userIds);
                        return ResponseEntity.ok().body(Map.of("message", "Users deleted successfully"));
                } catch (RuntimeException e) {
                        log.error("Error occurred while deleting users with IDs", userIds, e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "Unable to delete users."));
                } catch (Exception e) {
                        log.error("Unexpected error occurred while deleting users with IDs", userIds, e);
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "Unexpected error occurred."));
                }
        }

}