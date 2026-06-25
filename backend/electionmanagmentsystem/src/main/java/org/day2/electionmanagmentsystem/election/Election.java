package org.day2.electionmanagmentsystem.election;

import jakarta.persistence.*;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.common.enums.ElectionStatus;
import org.day2.electionmanagmentsystem.user.User;

import java.time.LocalDateTime;

@Entity
@Data
public class Election extends BaseEntity {

    private String description;
    @Column(nullable = false)
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @ManyToOne(optional = false)
    @JoinColumn(name="created_by_user_id",nullable = false)
    private User user;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ElectionStatus status;

}
