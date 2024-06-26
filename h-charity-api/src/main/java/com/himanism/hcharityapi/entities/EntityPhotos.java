package com.himanism.hcharityapi.entities;

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
@Table(name = "entity_photos")
@NoArgsConstructor
public class EntityPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String photoUrl;
    @Column(columnDefinition="tinyint(1) default 0")
    private Boolean isQRCode;
    private Long entityId;
    // @ManyToOne
    // @JoinColumn(name = "entity_id")
    // private Entities entity;
}
