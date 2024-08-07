package com.himanism.hcharityapi.entities;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;

    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "entity_id", referencedColumnName = "id")
    // private Entities entity;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    // private User user;

    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    private Entities entity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
