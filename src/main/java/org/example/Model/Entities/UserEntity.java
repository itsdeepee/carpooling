package org.example.Model.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNo;
    private LocalDate birthday;
    private String password;
}
