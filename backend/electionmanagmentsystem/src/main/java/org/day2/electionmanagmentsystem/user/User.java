package org.day2.electionmanagmentsystem.user;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.day2.electionmanagmentsystem.common.entity.BaseEntity;
import org.day2.electionmanagmentsystem.common.enums.UserStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends BaseEntity {

@Column(nullable = false)
private String firstName;
@Column(nullable = false)
private String lastName;
@Column(nullable = false)
private String email;
@Column(nullable = false)
private String  phone;
@Column(nullable = false)
private String passwordHash;
private String address;
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private UserStatus status;


}
