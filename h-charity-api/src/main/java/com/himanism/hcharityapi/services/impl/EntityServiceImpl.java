package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.himanism.hcharityapi.dto.request.EntityBankDetailsReqDto;
import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.response.EntityBankDetailsResDto;
import com.himanism.hcharityapi.dto.response.EntityPhotosDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.entities.Entities;
import com.himanism.hcharityapi.entities.EntityBankDetails;
import com.himanism.hcharityapi.entities.EntityPhotos;
import com.himanism.hcharityapi.mappers.EntityBankDetailsMapper;
import com.himanism.hcharityapi.mappers.EntityMapper;
import com.himanism.hcharityapi.repo.EntityBankDetailsRepo;
import com.himanism.hcharityapi.repo.EntityPhotosRepository;
import com.himanism.hcharityapi.repo.EntityRepository;
import com.himanism.hcharityapi.services.EntityService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService {

    private final EntityRepository entityRepository;
    private final EntityBankDetailsRepo bankDetailsRepo;
    private final EntityPhotosRepository photosRepository;

    @Override
    public List<EntityResponseDto> getEntities(Authentication authentication) {
        List<Entities> entities = entityRepository.findAll();
        return entities.stream().map(entity -> {
          EntityResponseDto entityResponseDto = EntityMapper.INSTANCE.entityToEntityResponseDTO(entity);
          EntityPhotosDto entityPhotosDto = getEntityPhotos(entity.getId());
          entityResponseDto.setEntityPhotos(entityPhotosDto);
          return entityResponseDto;
        }).collect(Collectors.toList());
    }

    @Override
    public Entities addEntity(EntityRequestDto entityRequestDto) {
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
        entity.setCreatedBy(entityRequestDto.getCreatedBy());
        entity.setCreatedDate(new Date());
        entity.setAddress(entityRequestDto.getAddress());
        return entityRepository.save(entity);
    }

    @Override
    public Entities updateEntity(EntityRequestDto entityRequestDto) {
        Optional<Entities> existingEntity = entityRepository.findById(entityRequestDto.getId());
        if (existingEntity.isEmpty()) throw new IllegalArgumentException("Invalid Entity ID");
        Entities updatedEntity = existingEntity.get();

        updatedEntity.setName(entityRequestDto.getName());
        updatedEntity.setType(entityRequestDto.getType());
        updatedEntity.setPresident(entityRequestDto.getPresident());
        updatedEntity.setPoc(entityRequestDto.getPoc());
        updatedEntity.setDescription(entityRequestDto.getDescription());
        updatedEntity.setIsVerified(entityRequestDto.getIsVerified());
        updatedEntity.setHasInternet(entityRequestDto.getHasInternet());        
        updatedEntity.setMobile(entityRequestDto.getMobile());
        updatedEntity.setOffice(entityRequestDto.getOffice());        
        updatedEntity.setUpdatedBy("Admin");
        updatedEntity.setUpdatedDate(new Date());          

        return entityRepository.save(updatedEntity);
    }

    @Override
    public void deleteEntity(Long entityId) {
         entityRepository.deleteById(entityId);
    }

    @Override
    public EntityResponseDto getEntityById(Long entityId) {
        EntityResponseDto entityResponseDto;
        Optional<Entities> optEntity = entityRepository.findById(entityId);
        Entities entity = optEntity.get();

        EntityPhotosDto entityPhotosDto = getEntityPhotos(entityId);
        EntityBankDetailsResDto bankDetailsResDto = this.getEntityBankDetails(entityId);
        
        entityResponseDto = EntityMapper.INSTANCE.entityToEntityResponseDTO(entity);
        entityResponseDto.setEntityPhotos(entityPhotosDto);
        entityResponseDto.setEntityBankDetails(bankDetailsResDto);
        
        return entityResponseDto;
    }

    private EntityPhotosDto getEntityPhotos(Long entityId) {
      Optional<List<EntityPhotos>> optEntityPhotos = photosRepository.findByEntityId(entityId);
      List<EntityPhotos> entityPhotos = optEntityPhotos.get();

      EntityPhotosDto entityPhotosDto = new EntityPhotosDto();

      List<String> lstEntityPhotos = new ArrayList<>();
      entityPhotos.stream()
      .forEach(photo -> {
        if(Boolean.TRUE.equals(photo.getIsQRCode())) {
          entityPhotosDto.setQrCode(photo.getPhotoUrl());
        }
        if(Boolean.TRUE.equals(photo.getIsCoverPhoto())) {
          entityPhotosDto.setCoverPhoto(photo.getPhotoUrl());
        }
        if(Boolean.FALSE.equals(photo.getIsQRCode()) && Boolean.FALSE.equals(photo.getIsCoverPhoto())) {
          lstEntityPhotos.add(photo.getPhotoUrl());
        }
      });
      entityPhotosDto.setPhotos(lstEntityPhotos);
      return entityPhotosDto;
    }

    private EntityBankDetailsResDto getEntityBankDetails(Long entityId) {
      EntityBankDetailsResDto entityBankDetailsResDto = new EntityBankDetailsResDto();

      Optional<EntityBankDetails> optEntityBankDetails = bankDetailsRepo.findByEntityId(entityId);
      
      if(optEntityBankDetails.isPresent()) {
        EntityBankDetails entityBankDetails = optEntityBankDetails.get();
        entityBankDetailsResDto = EntityBankDetailsMapper.INSTANCE.entityBankDetailsToEntityBankDetailsResDTO(entityBankDetails);
      }
      return entityBankDetailsResDto;
    }

    @Override
    public Optional<EntityBankDetails> getBankDetailsByEntityId(Long entityId) {
        return bankDetailsRepo.findByEntityId(entityId);
    }

    @Override
    public Optional<List<EntityPhotos>> getPhotosByEntityId(Long entityId) {
        return photosRepository.findByEntityId(entityId);
    }

    @Override
    public EntityBankDetailsResDto addEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username) {
      try {
        Optional<Entities> existingEntity = entityRepository.findById(bankDetailsReqDto.getEntityId());
        if (existingEntity.isEmpty()) throw new IllegalArgumentException("Invalid Entity ID");
        Entities fetchedEntity = existingEntity.get();

        EntityBankDetails entityBankDetails = EntityBankDetails.builder()
          .accountHolderName(bankDetailsReqDto.getAccountHolderName()).accountNo(bankDetailsReqDto.getAccountNo())
          .bankName(bankDetailsReqDto.getBankName()).branchName(bankDetailsReqDto.getBranchName())
          .ifscCode(bankDetailsReqDto.getIfscCode()).entity(fetchedEntity)
          .upiId(bankDetailsReqDto.getUpiId()).upiNumber(bankDetailsReqDto.getUpiNumber())
          .createdBy(username).createdDate(new Date())
          .build();       

        EntityBankDetails savedEntityBankDetails = bankDetailsRepo.save(entityBankDetails);
        return EntityBankDetailsResDto.builder()
        .accountHolderName(savedEntityBankDetails.getAccountHolderName()).accountNo(savedEntityBankDetails.getAccountNo())
        .bankName(savedEntityBankDetails.getBankName()).branchName(savedEntityBankDetails.getBranchName())
        .ifscCode(savedEntityBankDetails.getIfscCode())
        .upiId(savedEntityBankDetails.getUpiId()).upiNumber(savedEntityBankDetails.getUpiNumber())
        .build();
      } catch (Exception e) {
       throw e;
      }
    }

    @Override
    public EntityBankDetailsResDto updateEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username) {
      try {
        Optional<EntityBankDetails> optBankDetails = bankDetailsRepo.findById(bankDetailsReqDto.getId());
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
        return EntityBankDetailsResDto.builder()
        .accountHolderName(savedEntityBankDetails.getAccountHolderName()).accountNo(savedEntityBankDetails.getAccountNo())
        .bankName(savedEntityBankDetails.getBankName()).branchName(savedEntityBankDetails.getBranchName())
        .ifscCode(savedEntityBankDetails.getIfscCode())
        .upiId(savedEntityBankDetails.getUpiId()).upiNumber(savedEntityBankDetails.getUpiNumber())
        .build();
      } catch (Exception e) {
       throw e;
      }
    }
}
