package com.himanism.hcharityapi.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.common.Constants;
import com.himanism.hcharityapi.dto.request.EntityBankDetailsReqDto;
import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.request.EntityReviewReqDto;
import com.himanism.hcharityapi.dto.response.EntityBankDetailsResDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.dto.response.EntityReviewResDto;
import com.himanism.hcharityapi.entities.EntityBankDetails;
import com.himanism.hcharityapi.entities.EntityPhotos;
import com.himanism.hcharityapi.exception.AppException;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.himanism.hcharityapi.services.EntityService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entity")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class EntityController {

    private final EntityService entityService;

    @GetMapping("")
    public ResponseEntity<?> getEntities(Authentication authentication) {
        try {
            log.info("Entity Controller: List Entities");
            List<EntityResponseDto> entities = entityService.getEntities(authentication);
            return ResponseEntity.ok().body(entities);
        } catch (Exception e) {
            log.error("Error occurred while retrieving entities", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve entities");
        }
    }

    @GetMapping("/{entityId}")
    public EntityResponseDto getEntityById(@PathVariable Long entityId) {
        try {
            log.info("Entity Controller: Get Entity by ID");
            return entityService.getEntityById(entityId);
        } catch (Exception e) {
            log.error("Error occurred while retrieving entity with ID: " + entityId, e);
            throw new RuntimeException("Failed to retrieve entity");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addEntity(Authentication authentication, @Valid @RequestBody EntityRequestDto entityDto) {
        try {
            log.info("Entity Controller: Add Entity");
            UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return ResponseEntity.ok().body(entityService.addEntity(entityDto, principle));
        } catch (Exception e) {
            log.error("Error occurred while adding entity", e);
            throw new RuntimeException("Failed to add entity");
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateEntity(Authentication authentication,
            @Valid @RequestBody EntityRequestDto entityDto) {
        try {
            log.info("Entity Controller: Update Entity");
            Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetailsImpl) principle).getUsername();
            entityDto.setUpdatedBy(username);
            entityDto.setUpdatedDate(new Date());
            return ResponseEntity.ok().body(entityService.updateEntity(entityDto));
        } catch (Exception e) {
            log.error("Error occurred while updating entity", e);
            throw new RuntimeException("Failed to update entity");
        }
    }

    @DeleteMapping("/{entityId}")
    public void deleteEntity(Authentication authentication, @PathVariable Long entityId) {
        try {
            log.info("Entity Controller: Delete Entity");
            List<String> rolesFromToken = new ArrayList<>();

            Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Collection<? extends GrantedAuthority> roles = ((UserDetailsImpl) principle).getAuthorities();
            for (GrantedAuthority role : roles) {
                rolesFromToken.add(role.getAuthority());
            }

            if (rolesFromToken.stream().anyMatch(Constants.ROLES_CAN_DELETE_INSTITUTE::contains)) {
                entityService.deleteEntity(entityId);
            } else {
                throw new AppException("You do not have permission to delete this institute.", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting entity with ID", entityId, e);
            throw new RuntimeException("Failed to delete entity");
        }
    }

    @PostMapping("/bankDetails")
    public EntityBankDetailsResDto addEntityBankDetails(Authentication authentication,
            @Valid @RequestBody EntityBankDetailsReqDto bankDetailsReqDto) {
        try {
            log.info("Entity Controller: Add Entity Bank Details");
            Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetailsImpl) principle).getUsername();

            return entityService.addEntityBankDetails(bankDetailsReqDto, username);
        } catch (Exception e) {
            log.error("Error occurred while adding entity bank details for user: {}", authentication.getName(), e);
            throw new RuntimeException("Failed to add entity bank details");
        }
    }

    @PutMapping("/bankDetails")
    public EntityBankDetailsResDto updateEntityBankDetails(Authentication authentication,
            @Valid @RequestBody EntityBankDetailsReqDto bankDetailsReqDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();

        log.info("Entity Controller: Updating entity bank details", username);

        try {
            return entityService.updateEntityBankDetails(bankDetailsReqDto, username);
        } catch (Exception e) {
            log.error("Error updating entity bank details", username, e);
            throw e;
        }
    }

    @GetMapping("/bankDetails/{entityId}")
    public Optional<EntityBankDetails> getBankDetailsByEntityId(@PathVariable Long entityId) {
        log.info("Entity Controller: Fetching bank details for entity ID", entityId);

        try {
            return entityService.getBankDetailsByEntityId(entityId);
        } catch (Exception e) {
            log.error("Error fetching bank details for entity ID", entityId, e);
            throw e;
        }
    }

    @GetMapping("/photos/{entityId}")
    public Optional<List<EntityPhotos>> getPhotosByEntityId(@PathVariable Long entityId) {
        log.info("Entity Controller: Fetching photos for entity ID: {}", entityId);

        try {
            return entityService.getPhotosByEntityId(entityId);
        } catch (Exception e) {
            log.error("Error fetching photos for entity ID: {}", entityId, e);
            throw e;
        }
    }

    @GetMapping("/entityOwner/{entityOwnerId}")
    public ResponseEntity<?> checkIfInstituteOwnerExists(@PathVariable Long entityOwnerId) {
        log.info("Entity Controller: Checking if institute owner exists for entity owner ID", entityOwnerId);

        try {
            Boolean response = entityService.checkIfInstituteOwnerExists(entityOwnerId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Error checking if institute owner exists for entity owner ID", entityOwnerId, e);
            throw e; // Re-throwing the exception after logging
        }
    }

    @PostMapping("/entityReview")
    public ResponseEntity<?> saveEntityReview(@Valid @RequestBody EntityReviewReqDto entityReviewReqDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetailsImpl) principle).getUsername();

        log.info("Entity Controller: Saving entity review for user", username);

        try {
            return ResponseEntity.ok().body(entityService.saveEntityReview(entityReviewReqDto, username));
        } catch (Exception e) {
            log.error("Error saving entity review for user", username, e);
            throw e;
        }
    }

    @GetMapping("/entityReview/{entityId}")
    public ResponseEntity<?> getEntityReviews(@PathVariable Long entityId) {
        log.info("Entity Controller: Fetching entity reviews for entity ID", entityId);

        try {
            List<EntityReviewResDto> reviews = entityService.getEntityReviews(entityId);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            log.error("Error fetching entity reviews for entity ID", entityId, e);
            throw e;
        }
    }

}
