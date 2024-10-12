package com.himanism.hcharityapi.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.entities.Feedback;

public interface FeedbackService {

    List<FeedbackResDto> getFeedbacks(Authentication authentication);

    Feedback addFeedback(FeedbackRequestDto feedbackDto);

    Feedback updateFeedback(FeedbackRequestDto feedbackDto);

    void deleteFeedback(Long feedbackId);

    FeedbackResDto getFeedbackById(Long feedbackId);
}
