package com.himanism.hcharityapi.dto.request;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EntityBankDetailsReqDto {
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
