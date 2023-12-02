package org.example.Model.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "rides",uniqueConstraints = {@UniqueConstraint(columnNames = {"driver_id","dateTimeOfRide"})})
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long rideId;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocationEntity departureLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocationEntity destinationLocation;

    private LocalDateTime dateTimeOfRide;

    private int availableSeats;
    @NotBlank(message = "Status cannot be blank")
    private String rideStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", updatable = false, insertable = false)
    @JsonBackReference
    private DriverEntity driver;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ride_passengers",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> passengers;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="ride_id",nullable = false)
    @JsonManagedReference
    @ToString.Exclude
    private Set<RideRequestEntity> rideRequestEntities;


    private double cost;
    private List<String> additionalDetails;


}
