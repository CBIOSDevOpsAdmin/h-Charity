package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.entities.Entities;
import com.himanism.hcharityapi.entities.EntityFeedback;
import com.himanism.hcharityapi.entities.EntityFeedbackStatus;
import com.himanism.hcharityapi.mappers.FeedbackMapper;
import com.himanism.hcharityapi.repo.EntityFeedbackRepo;
import com.himanism.hcharityapi.repo.EntityFeedbackStatusRepo;
import com.himanism.hcharityapi.repo.EntityRepository;
import com.himanism.hcharityapi.services.EntityFeedbackService;

@Service
@Transactional
@RequiredArgsConstructor
public class EntityFeedbackServiceImpl implements EntityFeedbackService {

    private final EntityFeedbackRepo feedbackRepository;
    private final EntityFeedbackStatusRepo feedbackStatusRepo;
    private final EntityRepository entityRepository;

    // @Override
    // public List<FeedbackResDto> getFeedbacks(Authentication authentication) {
    // // Assuming that the authentication object can be used to filter feedback
    // based
    // // on user roles
    // List<EntityFeedback> feedbacks = feedbackRepository.findAll();
    // return feedbacks.stream()
    // .map(FeedbackMapper.INSTANCE::feedbackToFeedbackResponseDTO) // Assuming
    // FeedbackMapper exists
    // .collect(Collectors.toList());
    // }

    @Override
    @Transactional
    public EntityFeedback addFeedback(FeedbackRequestDto feedbackDto, String username) {
        EntityFeedback feedback = FeedbackMapper.INSTANCE.feedbackRequestDTOtoFeedback(feedbackDto);

        feedback.setAdvisedDate(new Date());

        Entities entity = entityRepository.findById(feedbackDto.getEntityId())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        feedback.setEntity(entity);

        List<EntityFeedbackStatus> lstFeedbackStatus = new ArrayList<>();
        EntityFeedbackStatus entityFeedbackStatus = EntityFeedbackStatus.builder()
                .status(feedbackDto.getStatus())
                .statusComment("")
                .statusCommenter(username)
                .statusCommentDate(new Date())
                .entityFeedback(feedback)
                .build();
        lstFeedbackStatus.add(entityFeedbackStatus);

        feedback.setEntityFeedbackStatusList(lstFeedbackStatus);
        feedbackRepository.save(feedback);

        return feedback;
    }

    @Override
    public EntityFeedback updateFeedback(FeedbackRequestDto feedbackDto) {
        Optional<EntityFeedback> existingFeedback = feedbackRepository.findById(feedbackDto.getId());
        if (existingFeedback.isEmpty()) {
            throw new IllegalArgumentException("Invalid Feedback ID");
        }
        EntityFeedback updatedFeedback = existingFeedback.get();
        updatedFeedback.setAdvisedBy(feedbackDto.getAdvisedBy());
        updatedFeedback.setAdvisedByContact(feedbackDto.getAdvisedByContact());
        updatedFeedback.setTitle(feedbackDto.getTitle());
        updatedFeedback.setDescription(feedbackDto.getDescription());
        updatedFeedback.setIsAnonymous(feedbackDto.getIsAnonymous());
        // updatedFeedback.setStatus(feedbackDto.getStatus());
        return feedbackRepository.save(updatedFeedback); // Save updated feedback to the repository
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId); // Delete feedback by ID
    }

    // @Override
    // public FeedbackResDto getFeedbackById(Long feedbackId) {
    // Optional<EntityFeedback> optFeedback =
    // feedbackRepository.findById(feedbackId);
    // if (optFeedback.isEmpty()) {
    // throw new IllegalArgumentException("Feedback not found");
    // }
    // EntityFeedback feedback = optFeedback.get();
    // return FeedbackMapper.INSTANCE.feedbackToFeedbackResponseDTO(feedback); //
    // Map entity to response DTO
    // }
}
