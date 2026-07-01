package org.day2.electionmanagmentsystem.vote;

import jakarta.persistence.*;
import lombok.Data;
import org.day2.electionmanagmentsystem.electioncandidate.ElectionCandidate;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames={"vote_transaction_id","candidate_id"})
})
@Data
public class VoteSelection extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name="vote_transaction_id",nullable = false)
    private VoteTransaction voteTransaction;
    @ManyToOne(optional = false)
    @JoinColumn(name="candidate_id",nullable = false)
    private ElectionCandidate candidate;



}
