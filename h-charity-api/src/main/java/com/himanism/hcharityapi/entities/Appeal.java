package com.himanism.hcharityapi.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "appeals")
@NoArgsConstructor
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Appeal tilte is required")
    @Size(min = 10, max = 40, message = "title should be minimum 10 and maximum 40 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private Boolean selfOrBehalf;

    @NotBlank(message = "On Behalf Name is required")
    private String onBehalfName;

    @NotBlank(message = "total Funds Required is required")
    private Integer totalFundsRequired;

    @NotBlank(message = "funds Received is required")
    private Integer fundsReceived;

    @NotBlank(message = "fundsNeeded is required")
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
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;
    private String isVerified;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    private Entities entity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
