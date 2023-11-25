package org.example.Model.DTOs.UserDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO {




    private Long ID;


    @NotBlank(message="First name cannot be black.")
    @Schema(description = "First name of user", example="Alice")
    private String firstName;

    @Schema(description="Middle name of user",example="J")
    private String middleName;

    @NotBlank(message="Last name cannot be black.")
    @Schema(description = "Last name of user",example="Blue")
    private String lastName;


    @Email(message = "Email is not valid")
    @NotBlank(message="Email cannot be black.")
    @Schema(description = "Email address of user", example="alice.blue@gmail.com")
    private String email;

    //maybe add validation for phone number
    @Size(min=9, max=15)
    @NotBlank(message="Phone number cannot be black.")
    @Pattern(regexp = "^(\\+\\d{2}( )?)?((\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$",
    message = "Phone number cannot contain letters or characters except for '+' sign to denote country code.")
    @Schema(description="Phone number of user",
            type="phone number",
            pattern ="^(\\+\\d{2}( )?)?((\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$",
            example = "+40 723 423 232")
    private String phoneNo;


    @Past(message = "Date must be in the past")
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Date cannot be null")
    @Schema(description = "Birthdate of user",type = "Date",pattern="yyyy-MM-dd")
    private LocalDate birthdate;

    @Size(min=6,max=30)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message="Password cannot be black.")
    @Schema(description = "Password for this user")
    private String password;




}
