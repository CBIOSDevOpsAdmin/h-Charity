package com.himanism.hcharityapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUser() {
        try {
            UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            log.info("User Controller: Fetching user details", principle.getId());

            UserResDto userResDto = userService.getUserById(principle.getId());

            if (userResDto != null) {
                log.info("Successfully fetched user details", principle.getId());
                return ResponseEntity.ok().body(userResDto);
            } else {
                log.error("User not found for user ID", principle.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching user details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user details");
        }
    }

    @GetMapping("/byRole")
    public ResponseEntity<?> getUserByRole(Erole role) {
        try {
            log.info("User Controller: Fetching users with role", role);

            List<UserResDto> userResDtos = userService.getUserByRole(role);

            if (userResDtos != null && !userResDtos.isEmpty()) {
                log.info("Successfully fetched users with role", userResDtos.size(), role);
                return ResponseEntity.ok().body(userResDtos);
            } else {
                log.warn("No users found with role", role);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with the specified role.");
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching users with role", role, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users with the specified role.");
        }
    }

}
