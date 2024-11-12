package com.himanism.hcharityapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedbackRequestDto {
    private Long id;
    private Long entityId;
    private String advisedBy;
    private String advisedByContact;
    private String title;
    private String description;
    private Boolean isAnonymous;
    private String status;
}
