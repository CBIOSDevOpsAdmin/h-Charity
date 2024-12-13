package com.himanism.hcharityapi.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.entities.Appeal;
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
        try {
            List<AppealResDto> appeals = appealService.getAppeals(authentication);
            return ResponseEntity.ok().body(appeals);
        } catch (Exception e) {
            log.error("An error occurred while fetching appeals", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching appeals");
        }
    }

    @GetMapping("/{appealId}")
    public ResponseEntity<?> getAppealById(@PathVariable Long appealId) {
        log.info("Appeals Controller: Fetching appeal with ID", appealId);
        try {
            AppealResDto appealResDto = appealService.getAppealById(appealId);
            return ResponseEntity.ok().body(appealResDto);
        } catch (Exception e) {
            log.error("An error occurred while fetching appeal with ID", appealId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching appeal");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addAppeal(Authentication authentication,
            @Valid @RequestBody AppealRequestDto appealRequestDto) {
        log.info("Appeals Controller: Adding new appeal for user");
        try {
            UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();

            String username = principle.getUsername();
            Long userId = principle.getId();
            List<String> roles = principle.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Appeal appeal = appealService.addAppeal(appealRequestDto, username, userId, roles.get(0));
            log.info("Appeals Controller: Appeal successfully added by user", username);
            return ResponseEntity.ok().body(appeal);
        } catch (Exception e) {
            log.error("An error occurred while adding an appeal", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding appeal");
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateEntity(Authentication authentication,
            @Valid @RequestBody AppealRequestDto appealRequestDto) {
        log.info("Appeals Controller: Updating appeal");
        try {
            Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username = ((UserDetailsImpl) principle).getUsername();
            // appealRequestDto.setUpdatedBy(username);
            // appealRequestDto.setUpdatedDate(new Date());

            Appeal updatedAppeal = appealService.updateAppeal(appealRequestDto);
            log.info("Appeal successfully updated by user", username);
            return ResponseEntity.ok().body(updatedAppeal);
        } catch (Exception e) {
            log.error("An error occurred while updating the appeal", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating appeal");
        }
    }

    @DeleteMapping("/{appealId}")
    public ResponseEntity<?> deleteAppeal(@PathVariable Long appealId) {
        log.info("Appeals Controller: Deleting appeal with ID", appealId);
        try {
            appealService.deleteAppeal(appealId);
            log.info("Appeal with ID successfully deleted", appealId);
            return ResponseEntity.ok().body("Appeal deleted successfully");
        } catch (Exception e) {
            log.error("An error occurred while deleting the appeal with ID {}: {}", appealId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting appeal");
        }
    }

}
