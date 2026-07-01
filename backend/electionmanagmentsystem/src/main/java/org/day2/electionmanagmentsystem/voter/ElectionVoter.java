package org.day2.electionmanagmentsystem.voter;

import jakarta.persistence.*;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.election.Election;
import org.day2.electionmanagmentsystem.user.User;
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "election_id","user_id"
        })
})
@Data
public class ElectionVoter extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "election_id",nullable = false)
    private Election election;
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

}


