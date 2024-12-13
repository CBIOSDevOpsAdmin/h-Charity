package com.himanism.hcharityapi.controllers;

import java.util.List;

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

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.EntityFeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entity/feedback")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class EntityFeedbackController {

    private final EntityFeedbackService feedbackService;

    // @GetMapping("/{entityId}")
    // public ResponseEntity<?> getFeedbacks(Authentication authentication,
    // @PathVariable Long entityId) {
    // log.info("Feedback Controller: List feedbacks");
    // List<FeedbackResDto> feedbacks = feedbackService.getFeedbacks(authentication,
    // entityId);
    // return ResponseEntity.ok().body(feedbacks);
    // }

    // @GetMapping("/{feedbackId}")
    // public FeedbackResDto getFeedbackById(@PathVariable Long feedbackId) {
    // return feedbackService.getFeedbackById(feedbackId);
    // }

    @PostMapping("")
    public ResponseEntity<?> addFeedback(Authentication authentication,
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
        String username = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = ((UserDetailsImpl) principal).getUsername();

            log.info("Entity Feedback Controller: Adding feedback", username);

            Object feedbackResponse = feedbackService.addFeedback(feedbackRequestDto, username);

            log.info("Feedback added successfully", username);
            return ResponseEntity.ok().body(feedbackResponse);

        } catch (Exception e) {
            log.error("Error occurred while adding feedback", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding feedback. Please try again later.");
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateFeedback(Authentication authentication,
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
        String username = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = ((UserDetailsImpl) principal).getUsername();

            log.info("Entity Feedback Controller: Updating feedback", username);

            Object feedbackResponse = feedbackService.updateFeedback(feedbackRequestDto);

            log.info("Feedback updated successfully", username);

            return ResponseEntity.ok().body(feedbackResponse);

        } catch (Exception e) {
            log.error("Error occurred while updating feedback", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating feedback. Please try again later.");
        }
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@PathVariable Long feedbackId) {
        try {
            feedbackService.deleteFeedback(feedbackId);
            log.info("Entity Feedback Controller: Feedback with ID: deleted successfully", feedbackId);
        } catch (Exception e) {
            log.error("Error occurred while deleting feedback with ID:", feedbackId, e);
            throw e;
        }
    }

}