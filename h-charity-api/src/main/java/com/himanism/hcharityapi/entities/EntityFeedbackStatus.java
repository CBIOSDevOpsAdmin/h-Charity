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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "entity_feedback_status")
@NoArgsConstructor
public class EntityFeedbackStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String status;
    private String statusComment;
    private String statusCommenter;
    private Date statusCommentDate;

    @ManyToOne
    @JoinColumn(name = "entity_feedback_id", nullable = false)
    @JsonBackReference
    private EntityFeedback entityFeedback;
}
