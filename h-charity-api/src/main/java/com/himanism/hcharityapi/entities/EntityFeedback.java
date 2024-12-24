package com.himanism.hcharityapi.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "entity_feedback")
@NoArgsConstructor
public class EntityFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Feedback title is required")
    private String title;

    @NotBlank(message = "description is required")
    private String description;
    private String advisedBy;
    private String advisedByContact;
    private Date advisedDate;
    private Boolean isAnonymous;

    @ManyToOne
    @JoinColumn(name = "entity_id", nullable = false)
    @JsonBackReference
    private Entities entity;

    @OneToMany(mappedBy = "entityFeedback", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EntityFeedbackStatus> entityFeedbackStatusList;
}
