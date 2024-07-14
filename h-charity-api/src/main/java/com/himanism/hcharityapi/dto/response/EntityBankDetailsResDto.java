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
public class EntityBankDetailsResDto {
    private Long id;
    private String accountHolderName;
    private String accountNo;
    private String bankName;
    private String branchName;
    private Long entityId;
    private String ifscCode;
    private String upiId;
    private String upiNumber;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;
}
