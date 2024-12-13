package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.himanism.hcharityapi.dto.request.EntityBankDetailsReqDto;
import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.request.EntityReviewReqDto;
import com.himanism.hcharityapi.dto.response.EntityBankDetailsResDto;
import com.himanism.hcharityapi.dto.response.EntityPhotosDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.dto.response.EntityReviewResDto;
import com.himanism.hcharityapi.entities.Entities;
import com.himanism.hcharityapi.entities.EntityBankDetails;
import com.himanism.hcharityapi.entities.EntityPhotos;
import com.himanism.hcharityapi.entities.EntityReview;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.mappers.EntityBankDetailsMapper;
import com.himanism.hcharityapi.mappers.EntityMapper;
import com.himanism.hcharityapi.repo.EntityBankDetailsRepo;
import com.himanism.hcharityapi.repo.EntityPhotosRepository;
import com.himanism.hcharityapi.repo.EntityRepository;
import com.himanism.hcharityapi.repo.EntityReviewRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.EntityService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService {

  private final EntityRepository entityRepository;
  private final EntityReviewRepository entityReviewRepository;
  private final EntityBankDetailsRepo bankDetailsRepo;
  private final EntityPhotosRepository photosRepository;
  private final UserRepository userRepository;

  @Value("${images.default-image}")
  private String defImgUrl;

  @Override
  public List<EntityResponseDto> getEntities(Authentication authentication) {
    log.info("EntityService: Fetching entities based on authentication");

    try {
      List<Entities> entities = getEntitiesBasedOnRole(authentication);

      return entities.stream().map(entity -> {
        EntityResponseDto entityResponseDto = EntityMapper.INSTANCE.entityToEntityResponseDTO(entity);
        EntityPhotosDto entityPhotosDto = getEntityPhotos(entity.getId());
        entityResponseDto.setEntityPhotos(entityPhotosDto);
        return entityResponseDto;
      }).collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error fetching entities based on authentication", e);
      throw e;
    }
  }

  @Override
  public Long addEntity(EntityRequestDto entityRequestDto, UserDetailsImpl principle) {
    log.info("EntityService: Adding new entity for user:", principle.getUsername());

    try {
      // TODO: Convert the below code using Mapper
      Entities entity = new Entities();
      entity.setName(entityRequestDto.getName());
      entity.setType(entityRequestDto.getType());
      entity.setPresident(entityRequestDto.getPresident());
      entity.setPoc(entityRequestDto.getPoc());
      entity.setDescription(entityRequestDto.getDescription());
      entity.setIsVerified(entityRequestDto.getIsVerified());
      entity.setHasInternet(entityRequestDto.getHasInternet());
      entity.setMobile(entityRequestDto.getMobile());
      entity.setOffice(entityRequestDto.getOffice());
      entity.setCreatedBy(principle.getUsername());
      entity.setCreatedDate(new Date());
      entity.setAddress(entityRequestDto.getAddress());

      User user = userRepository.findById(principle.getId()).orElseThrow(() -> new RuntimeException("User not found"));
      entity.setUser(user);
      entityRepository.save(entity);

      this.saveEntityPhotos(defImgUrl, entity.getId(), false, true);

      log.info("Entity added successfully with ID:", entity.getId());
      return entity.getId();
    } catch (Exception e) {
      log.error("Error adding entity for user:", principle.getUsername(), e);
      throw e;
    }
  }

  @Override
  public Long updateEntity(EntityRequestDto entityRequestDto) {
    log.info("EntityService: Updating entity with ID:", entityRequestDto.getId());

    try {
      Optional<Entities> existingEntity = entityRepository.findById(entityRequestDto.getId());
      if (existingEntity.isEmpty()) {
        log.error("Invalid Entity ID:", entityRequestDto.getId());
        throw new IllegalArgumentException("Invalid Entity ID");
      }
      Entities updatedEntity = existingEntity.get();

      User user = userRepository.findById(entityRequestDto.getEntityOwner())
          .orElseThrow(
              () -> new EntityNotFoundException("User not found with ID: " + entityRequestDto.getEntityOwner()));

      updatedEntity.setName(entityRequestDto.getName());
      updatedEntity.setType(entityRequestDto.getType());
      updatedEntity.setPresident(entityRequestDto.getPresident());
      updatedEntity.setPoc(entityRequestDto.getPoc());
      updatedEntity.setDescription(entityRequestDto.getDescription());
      updatedEntity.setIsVerified(entityRequestDto.getIsVerified());
      updatedEntity.setHasInternet(entityRequestDto.getHasInternet());
      updatedEntity.setMobile(entityRequestDto.getMobile());
      updatedEntity.setOffice(entityRequestDto.getOffice());
      updatedEntity.setAddress(entityRequestDto.getAddress());

      updatedEntity.setUser(user);

      entityRepository.save(updatedEntity);
      log.info("Entity updated successfully with ID:", updatedEntity.getId());
      return updatedEntity.getId();
    } catch (Exception e) {
      log.error("Error updating entity with ID:", entityRequestDto.getId(), e);
      throw e;
    }
  }

  @Override
  public void deleteEntity(Long entityId) {
    log.info("EntityService: Deleting entity with ID:", entityId);

    try {
      entityRepository.deleteById(entityId);
      log.info("Entity deleted successfully with ID:", entityId);
    } catch (Exception e) {
      log.error("Error deleting entity with ID:", entityId, e);
      throw e;
    }
  }

  @Override
  public EntityResponseDto getEntityById(Long entityId) {
    log.info("EntityService: Fetching entity details for entity ID:", entityId);

    try {
      EntityResponseDto entityResponseDto;
      Optional<Entities> optEntity = entityRepository.findById(entityId);

      if (optEntity.isEmpty()) {
        log.error("Entity not found with ID:", entityId);
        throw new RuntimeException("Entity not found");
      }

      Entities entity = optEntity.get();

      EntityPhotosDto entityPhotosDto = getEntityPhotos(entityId);
      EntityBankDetailsResDto bankDetailsResDto = this.getEntityBankDetails(entityId);

      entityResponseDto = EntityMapper.INSTANCE.entityToEntityResponseDTO(entity);
      entityResponseDto.setEntityPhotos(entityPhotosDto);
      entityResponseDto.setEntityBankDetails(bankDetailsResDto);

      log.info("Entity details fetched successfully for entity ID:", entityId);
      return entityResponseDto;
    } catch (Exception e) {
      log.error("Error fetching entity details for entity ID:", entityId, e);
      throw e;
    }
  }

  private EntityPhotosDto getEntityPhotos(Long entityId) {
    log.info("EntityService: Fetching photos for entity ID:", entityId);

    try {
      Optional<List<EntityPhotos>> optEntityPhotos = photosRepository.findByEntityId(entityId);

      if (optEntityPhotos.isEmpty()) {
        log.error("No photos found for entity ID:", entityId);
        throw new RuntimeException("No photos found for the given entity ID");
      }

      List<EntityPhotos> entityPhotos = optEntityPhotos.get();
      EntityPhotosDto entityPhotosDto = new EntityPhotosDto();

      List<String> lstEntityPhotos = new ArrayList<>();
      entityPhotos.forEach(photo -> {
        if (Boolean.TRUE.equals(photo.getIsQRCode())) {
          entityPhotosDto.setQrCode(photo.getPhotoUrl());
        }
        if (Boolean.TRUE.equals(photo.getIsCoverPhoto())) {
          entityPhotosDto.setCoverPhoto(photo.getPhotoUrl());
        }
        if (Boolean.FALSE.equals(photo.getIsQRCode()) && Boolean.FALSE.equals(photo.getIsCoverPhoto())) {
          lstEntityPhotos.add(photo.getPhotoUrl());
        }
      });
      entityPhotosDto.setPhotos(lstEntityPhotos);

      log.info("Photos fetched successfully for entity ID:", entityId);
      return entityPhotosDto;
    } catch (Exception e) {
      log.error("Error fetching photos for entity ID:", entityId, e);
      throw e;
    }
  }

  private EntityBankDetailsResDto getEntityBankDetails(Long entityId) {
    log.info("EntityService: Fetching bank details for entity ID:", entityId);

    try {
      EntityBankDetailsResDto entityBankDetailsResDto = new EntityBankDetailsResDto();

      Optional<EntityBankDetails> optEntityBankDetails = bankDetailsRepo.findByEntityId(entityId);

      if (optEntityBankDetails.isPresent()) {
        EntityBankDetails entityBankDetails = optEntityBankDetails.get();
        entityBankDetailsResDto = EntityBankDetailsMapper.INSTANCE
            .entityBankDetailsToEntityBankDetailsResDTO(entityBankDetails);
        log.info("Bank details fetched successfully for entity ID:", entityId);
      } else {
        log.warn("No bank details found for entity ID:", entityId);
      }

      return entityBankDetailsResDto;
    } catch (Exception e) {
      log.error("Error fetching bank details for entity ID:", entityId, e);
      throw e;
    }
  }

  @Override
  public Optional<EntityBankDetails> getBankDetailsByEntityId(Long entityId) {
    log.info("EntityService: Fetching bank details for entity ID:", entityId);

    try {
      Optional<EntityBankDetails> entityBankDetails = bankDetailsRepo.findByEntityId(entityId);

      if (entityBankDetails.isPresent()) {
        log.info("Bank details found for entity ID:", entityId);
      }

      return entityBankDetails;
    } catch (Exception e) {
      log.error("Error fetching bank details for entity ID:", entityId, e);
      throw e;
    }
  }

  @Override
  public Optional<List<EntityPhotos>> getPhotosByEntityId(Long entityId) {
    log.info("EntityService: Fetching photos for entity ID:", entityId);

    try {
      Optional<List<EntityPhotos>> entityPhotos = photosRepository.findByEntityId(entityId);

      if (entityPhotos.isPresent()) {
        log.info("Photos found for entity ID:", entityId);
      }

      return entityPhotos;
    } catch (Exception e) {
      log.error("Error fetching photos for entity ID:", entityId, e);
      throw e;
    }
  }

  @Override
  public EntityBankDetailsResDto addEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username) {
    log.info("EntityService: Adding bank details for entity ID:", bankDetailsReqDto.getEntityId());

    try {
      Optional<Entities> existingEntity = entityRepository.findById(bankDetailsReqDto.getEntityId());
      if (existingEntity.isEmpty()) {
        log.error("Invalid Entity ID:", bankDetailsReqDto.getEntityId());
        throw new IllegalArgumentException("Invalid Entity ID");
      }
      Entities fetchedEntity = existingEntity.get();

      EntityBankDetails entityBankDetails = EntityBankDetails.builder()
          .accountHolderName(bankDetailsReqDto.getAccountHolderName())
          .accountNo(bankDetailsReqDto.getAccountNo())
          .bankName(bankDetailsReqDto.getBankName())
          .branchName(bankDetailsReqDto.getBranchName())
          .ifscCode(bankDetailsReqDto.getIfscCode())
          .entity(fetchedEntity)
          .upiId(bankDetailsReqDto.getUpiId())
          .upiNumber(bankDetailsReqDto.getUpiNumber())
          .createdBy(username)
          .createdDate(new Date())
          .build();

      EntityBankDetails savedEntityBankDetails = bankDetailsRepo.save(entityBankDetails);
      log.info("Bank details added successfully for entity ID:", bankDetailsReqDto.getEntityId());

      return EntityBankDetailsResDto.builder()
          .accountHolderName(savedEntityBankDetails.getAccountHolderName())
          .accountNo(savedEntityBankDetails.getAccountNo())
          .bankName(savedEntityBankDetails.getBankName())
          .branchName(savedEntityBankDetails.getBranchName())
          .ifscCode(savedEntityBankDetails.getIfscCode())
          .upiId(savedEntityBankDetails.getUpiId())
          .upiNumber(savedEntityBankDetails.getUpiNumber())
          .build();
    } catch (Exception e) {
      log.error("Error adding bank details for entity ID:", bankDetailsReqDto.getEntityId(), e);
      throw e;
    }
  }

  @Override
  public EntityBankDetailsResDto updateEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username) {
    log.info("EntityService: Updating bank details for entity bank details ID:", bankDetailsReqDto.getId());

    try {
      Optional<EntityBankDetails> optBankDetails = bankDetailsRepo.findById(bankDetailsReqDto.getId());
      if (optBankDetails.isEmpty()) {
        log.error("Entity bank details not found with ID:", bankDetailsReqDto.getId());
        throw new EntityNotFoundException("Entity bank details not found with ID: " + bankDetailsReqDto.getId());
      }

      EntityBankDetails entityBankDetails = optBankDetails.get();

      entityBankDetails.setAccountHolderName(bankDetailsReqDto.getAccountHolderName());
      entityBankDetails.setAccountNo(bankDetailsReqDto.getAccountNo());
      entityBankDetails.setBankName(bankDetailsReqDto.getBankName());
      entityBankDetails.setBranchName(bankDetailsReqDto.getBranchName());
      entityBankDetails.setIfscCode(bankDetailsReqDto.getIfscCode());
      entityBankDetails.setUpiId(bankDetailsReqDto.getUpiId());
      entityBankDetails.setUpiNumber(bankDetailsReqDto.getUpiNumber());
      entityBankDetails.setUpdatedBy(username);
      entityBankDetails.setUpdatedDate(new Date());

      EntityBankDetails savedEntityBankDetails = bankDetailsRepo.save(entityBankDetails);
      log.info("Bank details updated successfully for entity bank details ID:", bankDetailsReqDto.getId());

      return EntityBankDetailsResDto.builder()
          .accountHolderName(savedEntityBankDetails.getAccountHolderName())
          .accountNo(savedEntityBankDetails.getAccountNo())
          .bankName(savedEntityBankDetails.getBankName())
          .branchName(savedEntityBankDetails.getBranchName())
          .ifscCode(savedEntityBankDetails.getIfscCode())
          .upiId(savedEntityBankDetails.getUpiId())
          .upiNumber(savedEntityBankDetails.getUpiNumber())
          .build();
    } catch (Exception e) {
      log.error("Error updating bank details for entity bank details ID:", bankDetailsReqDto.getId(), e);
      throw e;
    }
  }

  private void saveEntityPhotos(String url, Long entityId, Boolean isQRCode, Boolean isCoverPhoto) {
    log.info("EntityService: Saving entity photo for entity ID:, QR Code:, Cover Photo:", entityId, isQRCode,
        isCoverPhoto);

    try {
      EntityPhotos entityPhotos = new EntityPhotos();
      entityPhotos.setIsQRCode(isQRCode);
      entityPhotos.setPhotoUrl(url);
      entityPhotos.setEntityId(entityId);
      entityPhotos.setIsCoverPhoto(isCoverPhoto);

      photosRepository.save(entityPhotos);
      log.info("Entity photo saved successfully for entity ID:", entityId);
    } catch (Exception e) {
      log.error("Error saving entity photo for entity ID:", entityId, e);
      throw e;
    }
  }

  @Override
  public Boolean checkIfInstituteOwnerExists(Long entityOwnerId) {
    log.info("EntityService: Checking if institute owner exists with entity owner ID:", entityOwnerId);

    try {
      Optional<Entities> optEntity = entityRepository.findByUserId(entityOwnerId);

      Boolean exists = optEntity.isPresent();

      if (exists) {
        log.info("Institute owner exists for entity owner ID:", entityOwnerId);
      } else {
        log.info("No institute owner found for entity owner ID:", entityOwnerId);
      }

      return exists;
    } catch (Exception e) {
      log.error("Error checking if institute owner exists for entity owner ID:", entityOwnerId, e);
      throw e;
    }
  }

  private List<Entities> getEntitiesBasedOnRole(Authentication authentication) {
    Long userId = 0L;
    String role = "NORMAL_USER";

    try {
      if (authentication != null) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        userId = userDetails.getId();
        role = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList()).get(0);
      }

      log.info("User ID, Role", userId, role);

      if ("NORMAL_USER".equals(role) || "INSTITUTE_OWNER".equals(role)) {
        log.info("Fetching entities based on user role: {} with user ID: {}", role, userId);
        return entityRepository.findByIsVerifiedTrueOrUserId(userId);
      } else {
        log.info("Fetching all entities as the role is not NORMAL_USER or INSTITUTE_OWNER");
        return entityRepository.findAll();
      }
    } catch (Exception e) {
      log.error("Error fetching entities based on role and user ID:", userId, e);
      throw e;
    }
  }

  @Override
  public List<EntityReview> saveEntityReview(EntityReviewReqDto entityReviewReqDto, String username) {
    List<EntityReview> reviews = new ArrayList<>();
    try {
      log.info("EntityService: saving review for entity with ID:", entityReviewReqDto.getEntityId());

      Entities entity = entityRepository.findById(entityReviewReqDto.getEntityId())
          .orElseThrow(() -> {
            log.error("Entity with ID not found", entityReviewReqDto.getEntityId());
            return new EntityNotFoundException("Entity not found");
          });

      log.info("Entity found with ID:", entityReviewReqDto.getEntityId());

      EntityReview review = new EntityReview();
      review.setReviewStatus(entityReviewReqDto.getReviewStatus());
      review.setComment(entityReviewReqDto.getComment());
      review.setReviewedBy(username);
      review.setReviewDate(new Date());
      review.setEntity(entity);

      log.info("Review details: Status, Comment", entityReviewReqDto.getReviewStatus(),
          entityReviewReqDto.getComment());

      entity.getReviews().add(review);

      entity.setIsVerified("VERIFIED".equalsIgnoreCase(entityReviewReqDto.getReviewStatus()));

      log.info("Entity verification status updated to", entity.getIsVerified());

      entityRepository.save(entity);

      reviews = entity.getReviews();

      log.info("Review saved successfully for entity with ID", entityReviewReqDto.getEntityId());
    } catch (Exception e) {
      log.error("Error while saving entity review for entity with ID", entityReviewReqDto.getEntityId(), e);
      throw e;
    }

    return reviews;
  }

  @Override
  public List<EntityReviewResDto> getEntityReviews(Long entityId) {
    List<EntityReviewResDto> entityReviewResDtos = new ArrayList<>();
    try {
      log.info("EntityService: Fetching reviews for entity with ID", entityId);

      List<EntityReview> entityReviews = entityReviewRepository.findByEntityId(entityId);

      if (entityReviews.isEmpty()) {
        log.warn("No reviews found for entity with ID", entityId);
      } else {
        log.info("Found {} reviews for entity with ID", entityReviews.size(), entityId);
      }

      entityReviewResDtos = convertToDtoList(entityReviews);
    } catch (Exception e) {
      log.error("Error while fetching entity reviews for entity with ID", entityId, e);
      throw e;
    }
    return entityReviewResDtos;
  }

  public static List<EntityReviewResDto> convertToDtoList(List<EntityReview> entityReviews) {
    List<EntityReviewResDto> dtoList = new ArrayList<>();
    try {
      log.info("EntityService: Converting entity reviews to DTOs", entityReviews.size());

      dtoList = entityReviews.stream().map(review -> {
        EntityReviewResDto dto = new EntityReviewResDto();
        dto.setId(review.getId());
        dto.setReviewStatus(review.getReviewStatus());
        dto.setComment(review.getComment());
        dto.setReviewedBy(review.getReviewedBy());
        dto.setReviewDate(review.getReviewDate());
        return dto;
      }).collect(Collectors.toList());

      log.info("Successfully converted entity reviews to DTOs", dtoList.size());
    } catch (Exception e) {
      log.error("Error while converting entity reviews to DTOs", e);
      throw e;
    }
    return dtoList;
  }

}
