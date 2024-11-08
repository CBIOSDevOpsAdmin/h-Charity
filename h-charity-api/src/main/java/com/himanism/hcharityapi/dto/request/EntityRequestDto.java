package com.himanism.hcharityapi.dto.request;

import java.util.Date;
import java.util.List;

import com.himanism.hcharityapi.entities.Address;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityRequestDto {
    private Long id;
    private String name;
    private String type;
    private String president;
    private String poc;
    private String description;
    private Long entityOwner;
    private Boolean isVerified;
    private Boolean hasInternet;
    private List<String> photos;
    private String mobile;
    private String office;
    private Address address;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;
}
