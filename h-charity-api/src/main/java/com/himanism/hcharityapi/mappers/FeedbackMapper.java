package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.request.FeedbackRequestDto;
import com.himanism.hcharityapi.dto.response.FeedbackResDto;
import com.himanism.hcharityapi.entities.Feedback;

@Mapper
public interface FeedbackMapper {

    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    FeedbackResDto feedbackToFeedbackResponseDTO(Feedback feedback);

    Feedback feedbackResponseDTOToFeedback(FeedbackResDto feedbackResDto);

    Feedback feedbackRequestDTOtoFeedback(FeedbackRequestDto feedbackRequestDto);
}
