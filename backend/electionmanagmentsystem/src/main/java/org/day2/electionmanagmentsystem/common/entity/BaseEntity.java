package org.day2.electionmanagmentsystem.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,updatable = false)
    private UUID publicId;
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
@PrePersist
    protected void onCreate(){
        LocalDateTime now=LocalDateTime.now();
        this.publicId=UUID.randomUUID();
        this.createdAt=now;
        this.updatedAt=now;
    }
    @PreUpdate
    protected void onUpdate(){
        LocalDateTime now=LocalDateTime.now();
        this.updatedAt=now;
    }
}
