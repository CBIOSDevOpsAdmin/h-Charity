package com.himanism.hcharityapi.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.request.AppealRequestDto;
import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.entities.Appeal;
import com.himanism.hcharityapi.entities.Entities;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.AppealService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appeal")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class AppealController {

    private final AppealService appealService;

    @GetMapping("")
    public ResponseEntity<?> getAppeals(Authentication authentication) {
        log.info("Appeals Controller: List appeals");
        List<AppealResDto> appeals = appealService.getAppeals(authentication);
        return ResponseEntity.ok().body(appeals);
    }

    @GetMapping("/{appealId}")
    public AppealResDto getAppealById(@PathVariable Long appealId) {
        // if (user.getEmail() == null || user.getEmail().isEmpty() ||
        // user.getPassword() == null || user.getPassword().isEmpty()) {
        // throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        // }
        AppealResDto appealResDto = appealService.getAppealById(appealId);
        return appealResDto;
    }

    @PostMapping("")
    public Appeal addAppeal(Authentication authentication, @Valid @RequestBody AppealRequestDto appealRequestDto) {
        // if (user.getEmail() == null || user.getEmail().isEmpty() ||
        // user.getPassword() == null || user.getPassword().isEmpty()) {
        // throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        // }

        // Make proper use of Lombok validators

        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   
        String username = ((UserDetailsImpl) principle).getUsername();

        return appealService.addAppeal(appealRequestDto);
    }

    @PutMapping("")
    public Appeal updateEntity(Authentication authentication, @Valid @RequestBody AppealRequestDto appealRequestDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();
        // appealRequestDto.setUpdatedBy(username);
        // appealRequestDto.setUpdatedDate(new Date());

        return appealService.updateAppeal(appealRequestDto);
    }

    @DeleteMapping("/{appealId}")
    public void deleteAppeal(@PathVariable Long appealId) {
        // if (user.getEmail() == null || user.getEmail().isEmpty() ||
        // user.getPassword() == null || user.getPassword().isEmpty()) {
        // throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        // }

        appealService.deleteAppeal(appealId);
    }


}
