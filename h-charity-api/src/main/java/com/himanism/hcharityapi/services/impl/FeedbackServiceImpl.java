package com.himanism.hcharityapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.entities.Feedback;
import com.himanism.hcharityapi.mappers.FeedbackMapper; // Assuming a mapper for Feedback exists
import com.himanism.hcharityapi.repo.FeedbackRepository;
import com.himanism.hcharityapi.services.FeedbackService;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<FeedbackResDto> getFeedbacks(Authentication authentication) {
        // Assuming that the authentication object can be used to filter feedback based
        // on user roles
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream()
                .map(FeedbackMapper.INSTANCE::feedbackToFeedbackResponseDTO) // Assuming FeedbackMapper exists
                .collect(Collectors.toList());
    }

    @Override
    public Feedback addFeedback(FeedbackRequestDto feedbackDto) {
        Feedback feedback = FeedbackMapper.INSTANCE.feedbackRequestDTOtoFeedback(feedbackDto); // Map DTO to Entity
        return feedbackRepository.save(feedback); // Save feedback to the repository
    }

    @Override
    public Feedback updateFeedback(FeedbackRequestDto feedbackDto) {
        Optional<Feedback> existingFeedback = feedbackRepository.findById(feedbackDto.getId());
        if (existingFeedback.isEmpty()) {
            throw new IllegalArgumentException("Invalid Feedback ID");
        }
        Feedback updatedFeedback = existingFeedback.get();
        updatedFeedback.setName(feedbackDto.getName());
        updatedFeedback.setContactNumber(feedbackDto.getContactNumber());
        updatedFeedback.setTitle(feedbackDto.getTitle());
        updatedFeedback.setDescription(feedbackDto.getDescription());
        updatedFeedback.setIsAnonymous(feedbackDto.getIsAnonymous());
        updatedFeedback.setStatus(feedbackDto.getStatus());
        return feedbackRepository.save(updatedFeedback); // Save updated feedback to the repository
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId); // Delete feedback by ID
    }

    @Override
    public FeedbackResDto getFeedbackById(Long feedbackId) {
        Optional<Feedback> optFeedback = feedbackRepository.findById(feedbackId);
        if (optFeedback.isEmpty()) {
            throw new IllegalArgumentException("Feedback not found");
        }
        Feedback feedback = optFeedback.get();
        return FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback); // Map entity to response DTO
    }
}
