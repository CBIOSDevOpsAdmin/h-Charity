package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.entities.EntityFeedback;

@Mapper
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    FeedbackResDto feedbackToFeedbackResponseDTO(EntityFeedback feedback);

    EntityFeedback feedbackResponseDTOToFeedback(FeedbackResDto feedbackResDto);

    @Mapping(target = "advisedDate", ignore = true)
    @Mapping(target = "entityFeedbackStatusList", ignore = true)
    @Mapping(target = "entity", ignore = true)
    EntityFeedback feedbackRequestDTOtoFeedback(FeedbackRequestDto feedbackRequestDto);
}
