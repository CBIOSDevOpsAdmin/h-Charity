package com.himanism.hcharityapi.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "entities")
@NoArgsConstructor
public class Entities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Entity Name is required")
    private String name;

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "president is required")
    private String president;
    private String poc;

    @NotBlank(message = "description is required")
    private String description;

    private Boolean isVerified;

    private Boolean hasInternet;

    @NotBlank(message = "Mobile Number is required")
    private String mobile;
    private String office;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String deletedBy;
    private Date deletedDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entity_owner_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EntityReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Appeal> appeals = new ArrayList<>();

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EntityFeedback> feedbacks;

}