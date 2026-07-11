package org.day2.electionmanagmentsystem.electioncandidate;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.day2.electionmanagmentsystem.position.ElectionPosition;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.user.User;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"position_id","user_id"})
})
@Data

public class ElectionCandidate extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name="position_id",nullable = false)
    private ElectionPosition position;
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "added_by", nullable = false)

    private User addedBy;

}
