package com.himanism.hcharityapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.himanism.hcharityapi.dto.request.EntityBankDetailsReqDto;
import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.request.EntityReviewReqDto;
import com.himanism.hcharityapi.dto.response.EntityBankDetailsResDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.dto.response.EntityReviewResDto;
import com.himanism.hcharityapi.entities.EntityBankDetails;
import com.himanism.hcharityapi.entities.EntityPhotos;
import com.himanism.hcharityapi.entities.EntityReview;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;

public interface EntityService {
    List<EntityResponseDto> getEntities(Authentication authentication);

    Long addEntity(EntityRequestDto entityDto, UserDetailsImpl principle);

    Long updateEntity(EntityRequestDto entityDto);

    void deleteEntity(Long entityId);

    EntityResponseDto getEntityById(Long entityId);

    Optional<EntityBankDetails> getBankDetailsByEntityId(Long entityId);

    Optional<List<EntityPhotos>> getPhotosByEntityId(Long entityId);

    EntityBankDetailsResDto addEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username);

    EntityBankDetailsResDto updateEntityBankDetails(EntityBankDetailsReqDto bankDetailsReqDto, String username);

    Boolean checkIfInstituteOwnerExists(Long entityOwnerId);

    List<EntityReview> saveEntityReview(EntityReviewReqDto entityReviewReqDto, String username);

    List<EntityReviewResDto> getEntityReviews(Long entityId);
}
