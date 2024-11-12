package com.himanism.hcharityapi.controllers;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principal).getUsername();

        return ResponseEntity.ok().body(feedbackService.addFeedback(feedbackRequestDto, username));
    }

    @PutMapping("")
    public ResponseEntity<?> updateFeedback(Authentication authentication,
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principal).getUsername();
        return ResponseEntity.ok().body(feedbackService.updateFeedback(feedbackRequestDto));
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
    }
}