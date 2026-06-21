package org.day2.electionmanagmentsystem.Position;
import jakarta.persistence.*;
import lombok.Data;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.election.Election;
@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"election_id","name"}
        )
})
public class ElectionPosition extends BaseEntity {
@ManyToOne(optional = false)
@JoinColumn(name="election_id",nullable = false)
private Election election;
@Column(nullable = false)
private String name;
@Column(nullable = false)
private String description;
@Column(nullable = false)
private int minSelection;
@Column(nullable = false)
private int maxSelection;
}
