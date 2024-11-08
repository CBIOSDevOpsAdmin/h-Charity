package com.himanism.hcharityapi.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityReviewResDto {
    private Long id;
    private String reviewStatus;
    private String comment;
    private String reviewedBy;
    private Date reviewDate;
}
