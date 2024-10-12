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
import com.himanism.hcharityapi.entities.Feedback;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.FeedbackService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("")
    public ResponseEntity<?> getFeedbacks(Authentication authentication) {
        log.info("Feedback Controller: List feedbacks");
        List<FeedbackResDto> feedbacks = feedbackService.getFeedbacks(authentication);
        return ResponseEntity.ok().body(feedbacks);
    }

    @GetMapping("/{feedbackId}")
    public FeedbackResDto getFeedbackById(@PathVariable Long feedbackId) {
        FeedbackResDto feedbackResDto = feedbackService.getFeedbackById(feedbackId);
        return feedbackResDto;
    }

    @PostMapping("")
    public Feedback addFeedback(Authentication authentication,
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principal).getUsername();

        return feedbackService.addFeedback(feedbackRequestDto);
    }

    @PutMapping("")
    public Feedback updateFeedback(Authentication authentication,
            @Valid @RequestBody FeedbackRequestDto feedbackRequestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principal).getUsername();

        return feedbackService.updateFeedback(feedbackRequestDto);
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
    }
}