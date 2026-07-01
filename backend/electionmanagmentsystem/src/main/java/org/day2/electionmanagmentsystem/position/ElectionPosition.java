package org.day2.electionmanagmentsystem.position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.election.Election;
import org.hibernate.annotations.Check;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"election_id","name"}
        )
})
@Check(constraints = """
                min_selection >= 0
                AND max_selection > 0
                AND max_selection >= min_selection
                """)
public class ElectionPosition extends BaseEntity {
@ManyToOne(optional = false)
@JoinColumn(name="election_id",nullable = false)
private Election election;
@Column(nullable = false,length = 100)
private String name;
@Column(nullable = false)
private String description;
@Column(nullable = false)
private int minSelection;
@Column(nullable = false)
private int maxSelection;
}
