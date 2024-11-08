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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "entity_review")
@NoArgsConstructor
public class EntityReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String reviewStatus;
    private String comment;
    private String reviewedBy;
    private Date reviewDate;

    @ManyToOne
    @JoinColumn(name = "entity_id", nullable = false)
    @JsonBackReference
    private Entities entity;
}
