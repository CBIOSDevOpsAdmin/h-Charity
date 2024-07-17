package com.himanism.hcharityapi.dto.response;

import java.util.Date;

import com.himanism.hcharityapi.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityResponseDto {
    private Long id;
    private String name;
    private String type;
    private String president;
    private String poc;
    private String description;
    private Boolean isVerified;
    private Boolean hasInternet;
    private String mobile;
    private String office;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate; 
    private Address address;
    private EntityPhotosDto entityPhotos;
    private EntityBankDetailsResDto entityBankDetails;
}
