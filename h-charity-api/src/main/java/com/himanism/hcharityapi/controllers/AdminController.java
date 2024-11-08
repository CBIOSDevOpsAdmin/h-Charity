package com.himanism.hcharityapi.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.dto.response.MessageResponseDto;
import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.Entities;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.security.services.UserDetailsServiceImpl;
import com.himanism.hcharityapi.services.AdminService;
import com.himanism.hcharityapi.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/user")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class AdminController {
        @Autowired
        UserRepository userRepository;

        @Autowired
        RoleRepository roleRepository;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        private final AdminService adminService;

        @PostMapping("")
        public ResponseEntity<?> saveUser(@Valid @RequestBody UserReqDto userReqDto) {
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

                String role = userReqDto.getRole();
                Set<Role> roles = new HashSet<>();

                switch (role) {
                        case "ADMIN":
                                Role adminRole = roleRepository.findByName(Erole.ADMIN)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(adminRole);

                                break;
                        case "ORGANISATION_VOLUNTEER":
                                Role modRole = roleRepository.findByName(Erole.ORGANISATION_VOLUNTEER)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(modRole);

                                break;
                        case "INSTITUTE_OWNER":
                                Role insOwnerRole = roleRepository.findByName(Erole.INSTITUTE_OWNER)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(insOwnerRole);

                                break;
                        default:
                                Role userRole = roleRepository.findByName(Erole.NORMAL_USER)
                                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(userRole);
                }

                user.setRoles(roles);
                userRepository.save(user);

                return ResponseEntity.ok(new MessageResponseDto("User created successfully!"));
        }

        @GetMapping("")
        public ResponseEntity<?> getUsers(Authentication authentication) {
                log.info("Admin Controller: List Users");
                List<UserResDto> users = adminService.getUsers(authentication);
                return ResponseEntity.ok().body(users);
        }

        @PutMapping("")
        public ResponseEntity<?> updateUser(Authentication authentication, @Valid @RequestBody UserReqDto userReqDto) {
                try {
                        UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext()
                                        .getAuthentication()
                                        .getPrincipal();
                        UserResDto userResDto = adminService.updateUser(userReqDto, principle);
                        return ResponseEntity.ok().body(userResDto);
                } catch (Exception e) {
                        return ResponseEntity
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(e);
                }
        }
}