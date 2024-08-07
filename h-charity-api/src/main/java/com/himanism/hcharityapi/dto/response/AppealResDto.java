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
public class AppealResDto {
  private Long id;
    private String title;
    private String description;
    private Boolean selfOrBehalf;
    private String onBehalfName;
    private Integer totalFundsRequired;
    private Integer fundsReceived;
    private Integer fundsNeeded;
    private Boolean isZakatEligible;
    private Boolean isInterestEligible;
    private Boolean isAnonymous;
    private String appealer;
    private String appealerMobile;
    private Date requirementDate;
    private String verifier;
    private String verifierMobile;
    private Date verifiedDate;
}
