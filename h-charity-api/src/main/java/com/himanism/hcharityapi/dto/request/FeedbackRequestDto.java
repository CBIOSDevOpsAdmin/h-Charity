package com.himanism.hcharityapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedbackRequestDto {
    private Long id;
    private String name;
    private String contactNumber;
    private String title;
    private String description;
    private Boolean isAnonymous;
    private String status;
}
