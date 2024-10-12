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
public class FeedbackResDto {
    private Long id;
    private String name;
    private String contactNumber;
    private String title;
    private String description;
    private Boolean isAnonymous;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;
}
