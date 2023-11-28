package org.example.Model.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="drivers")

public class DriverEntity {

    @Id
    private Long id;

    @OneToOne

    @JoinColumn(name="user_id")
    private UserEntity user;

    @NotNull
    @Column(unique = true)
    private String driverLicenseNumber;
    @NotNull
    private String vehicleType;


    @OneToMany(mappedBy = "driver", fetch=FetchType.LAZY)
    @JsonIgnore
    private Set<RideEntity> rides;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<LocationEntity> recentAddresses;



}
