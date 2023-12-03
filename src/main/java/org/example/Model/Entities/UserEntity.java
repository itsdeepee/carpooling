package org.example.Model.Entities;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@ToString
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String fullName;
    @Email
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String phoneNo;
    @Temporal(TemporalType.DATE)
    @NotNull
    private LocalDate birthdate;
    private String password;
    private String role;

}
