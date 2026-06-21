package org.day2.electionmanagmentsystem.vote;

import jakarta.persistence.*;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.common.enums.VoteActionType;
import org.day2.electionmanagmentsystem.user.User;

import java.time.LocalDateTime;
@Entity
public class VoteAction extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_transaction_id",nullable = false)
    private VoteTransaction voteTransaction;
    @ManyToOne(optional = false)
    @JoinColumn(name="performed_by_user_id")
    private User performedBy;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoteActionType actionType;
    @Column(length = 1000)
    private String reason;
    @Column(nullable = false)
    private LocalDateTime performedAt;
    @Column(length=500)
    private String photoKey;
}
