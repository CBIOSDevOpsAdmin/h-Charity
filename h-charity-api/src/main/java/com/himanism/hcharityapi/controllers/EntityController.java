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
        log.info("Entity Controller: List Entities");
        List<EntityResponseDto> entities = entityService.getEntities(authentication);
        return ResponseEntity.ok().body(entities);
    }

    @GetMapping("/{entityId}")
    public EntityResponseDto getEntityById(@PathVariable Long entityId) {
        return entityService.getEntityById(entityId);
    }

    @PostMapping("")
    public ResponseEntity<?> addEntity(Authentication authentication, @Valid @RequestBody EntityRequestDto entityDto) {
        // if (user.getEmail() == null || user.getEmail().isEmpty() ||
        // user.getPassword() == null || user.getPassword().isEmpty()) {
        // throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        // }

        // Make proper use of Lombok validators

        UserDetailsImpl principle = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok().body(entityService.addEntity(entityDto, principle));
    }

    @PutMapping("")
    public ResponseEntity<?> updateEntity(Authentication authentication,
            @Valid @RequestBody EntityRequestDto entityDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();
        entityDto.setUpdatedBy(username);
        entityDto.setUpdatedDate(new Date());

        return ResponseEntity.ok().body(entityService.updateEntity(entityDto));
    }

    @DeleteMapping("/{entityId}")
    public void deleteEntity(Authentication authentication, @PathVariable Long entityId) {
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
    }

    @PostMapping("/bankDetails")
    public EntityBankDetailsResDto addEntityBankDetails(Authentication authentication,
            @Valid @RequestBody EntityBankDetailsReqDto bankDetailsReqDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();

        return entityService.addEntityBankDetails(bankDetailsReqDto, username);
    }

    @PutMapping("/bankDetails")
    public EntityBankDetailsResDto updateEntityBankDetails(Authentication authentication,
            @Valid @RequestBody EntityBankDetailsReqDto bankDetailsReqDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();

        return entityService.updateEntityBankDetails(bankDetailsReqDto, username);
    }

    @GetMapping("/bankDetails/{entityId}")
    public Optional<EntityBankDetails> getBankDetailsByEntityId(@PathVariable Long entityId) {
        // if (user.getEmail() == null || user.getEmail().isEmpty() ||
        // user.getPassword() == null || user.getPassword().isEmpty()) {
        // throw new AppException("All fields are required.", HttpStatus.BAD_REQUEST);
        // }

        return entityService.getBankDetailsByEntityId(entityId);
    }

    @GetMapping("/photos/{entityId}")
    public Optional<List<EntityPhotos>> getPhotosByEntityId(@PathVariable Long entityId) {
        return entityService.getPhotosByEntityId(entityId);
    }

    @GetMapping("/entityOwner/{entityOwnerId}")
    public ResponseEntity<?> checkIfInstituteOwnerExists(@PathVariable Long entityOwnerId) {
        Boolean response = entityService.checkIfInstituteOwnerExists(entityOwnerId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/entityReview")
    public ResponseEntity<?> saveEntityReview(@Valid @RequestBody EntityReviewReqDto entityReviewReqDto) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetailsImpl) principle).getUsername();
        return ResponseEntity.ok().body(entityService.saveEntityReview(entityReviewReqDto, username));
    }

    @GetMapping("/entityReview/{entityId}")
    public ResponseEntity<?> getEntityReviews(@PathVariable Long entityId) {
        List<EntityReviewResDto> reviews = entityService.getEntityReviews(entityId);
        return ResponseEntity.ok().body(reviews);
    }
}
