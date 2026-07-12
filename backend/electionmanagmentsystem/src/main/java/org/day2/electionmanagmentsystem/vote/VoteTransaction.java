package org.day2.electionmanagmentsystem.vote;

import jakarta.persistence.*;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.common.enums.VerificationProvider;
import org.day2.electionmanagmentsystem.common.enums.VoteStatus;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.voter.Voter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data

public class VoteTransaction extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name="election_id",nullable = false)
    private Election election;
    @ManyToOne(optional = false)
    @JoinColumn(name="voter_id",nullable = false)
    private Voter voter;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteStatus status;

    @ManyToOne
    @JoinColumn(name="parent_vote_transaction_id")
    private VoteTransaction parentVoteTransaction;
    private LocalDateTime submittedAt;
    @Column(precision = 5,scale = 4)
    private BigDecimal verificationScore;
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private VerificationProvider verificationProvider;



}
