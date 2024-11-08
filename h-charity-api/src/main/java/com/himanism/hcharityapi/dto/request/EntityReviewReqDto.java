package com.himanism.hcharityapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityReviewReqDto {
    private Long id;
    private String reviewStatus;
    private String comment;
    private Long entityId;

}
