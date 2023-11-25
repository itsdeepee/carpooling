package org.example.Model.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="drivers")
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="driver_id")
    private Long driverId;

    @NotNull
    @Column(unique = true)
    private String driverLicenseNumber;
    @NotNull
    private String vehicleType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity userEntity;

}
