package com.himanism.hcharityapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

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
        UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        UserResDto userResDto = userService.getUserById(principle.getId());
        return ResponseEntity.ok().body(userResDto);
    }

    @GetMapping("/byRole")
    public ResponseEntity<?> getUserByRole(Erole role) {
        List<UserResDto> userResDtos = userService.getUserByRole(role);
        return ResponseEntity.ok().body(userResDtos);
    }

}
