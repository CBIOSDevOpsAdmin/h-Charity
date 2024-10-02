package com.himanism.hcharityapi.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.MessageResponseDto;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsServiceImpl;

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

        @PostMapping("")
        public ResponseEntity<?> registerUser(@Valid @RequestBody UserReqDto userReqDto) {
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

                return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
        }
}