package org.day2.electionmanagmentsystem.vote;

import jakarta.persistence.*;
import org.day2.electionmanagmentsystem.candidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames={"vote_transaction_id","candidate_id"})
})
public class VoteSelection extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name="vote_transaction_id",nullable = false)
    private VoteTransaction voteTransaction;
    @ManyToOne(optional = false)
    @JoinColumn(name="candidate_id",nullable = false)
    private ElectionCandidate candidate;



}
