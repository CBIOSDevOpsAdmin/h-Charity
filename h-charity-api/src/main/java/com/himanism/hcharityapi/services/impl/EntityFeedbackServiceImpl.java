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
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
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
        try {
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

            log.info("Entity Feedback Service: Feedback successfully added", feedbackDto.getEntityId());
        } catch (EntityNotFoundException e) {
            log.error("Entity with ID {} not found when adding feedback", feedbackDto.getEntityId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while adding feedback", feedbackDto.getEntityId(), e);
            throw new RuntimeException("Error adding feedback", e);
        }

        return feedback;
    }

    @Override
    public EntityFeedback updateFeedback(FeedbackRequestDto feedbackDto) {
        try {
            Optional<EntityFeedback> existingFeedback = feedbackRepository.findById(feedbackDto.getId());
            if (existingFeedback.isEmpty()) {
                log.error("Feedback with ID {} not found", feedbackDto.getId());
                throw new IllegalArgumentException("Invalid Feedback ID");
            }

            EntityFeedback updatedFeedback = existingFeedback.get();
            updatedFeedback.setAdvisedBy(feedbackDto.getAdvisedBy());
            updatedFeedback.setAdvisedByContact(feedbackDto.getAdvisedByContact());
            updatedFeedback.setTitle(feedbackDto.getTitle());
            updatedFeedback.setDescription(feedbackDto.getDescription());
            updatedFeedback.setIsAnonymous(feedbackDto.getIsAnonymous());
            // updatedFeedback.setStatus(feedbackDto.getStatus());

            EntityFeedback savedFeedback = feedbackRepository.save(updatedFeedback);
            log.info("Entity Feedback Service: Feedback with ID  successfully updated", feedbackDto.getId());
            return savedFeedback;

        } catch (IllegalArgumentException e) {
            log.error("Error updating feedback with ID", feedbackDto.getId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating feedback with ID", feedbackDto.getId(), e);
            throw new RuntimeException("Error updating feedback", e);
        }
    }

    @Override
    public void deleteFeedback(Long feedbackId) {
        try {
            log.info("Entity Feedback Service: delete feedback with ID", feedbackId);

            if (!feedbackRepository.existsById(feedbackId)) {
                log.error("Feedback with ID, not found", feedbackId);
                throw new IllegalArgumentException("Feedback not found");
            }

            feedbackRepository.deleteById(feedbackId); // Delete feedback by ID
            log.info("Feedback with ID, successfully deleted", feedbackId);

        } catch (IllegalArgumentException e) {
            log.error("Error deleting feedback with ID {}: {}", feedbackId, e.getMessage());
            throw e; // Rethrow exception after logging
        } catch (Exception e) {
            log.error("Unexpected error occurred while deleting feedback with ID", feedbackId, e);
            throw new RuntimeException("Error deleting feedback", e);
        }
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
