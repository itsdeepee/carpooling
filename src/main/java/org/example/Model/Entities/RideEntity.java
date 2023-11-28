package org.example.Model.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name="rides")
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ride_id")
    private Long rideId;

    @ManyToOne(fetch=FetchType.LAZY)
    private LocationEntity departureLocation;

    @ManyToOne(fetch=FetchType.LAZY)
    private LocationEntity destinationLocation;

    @Column(unique = true)
    private LocalDateTime dateTimeOfRide;


    private int availableSeats;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="driver_id")
    private DriverEntity driver;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="ride_passengers",
            joinColumns=@JoinColumn(name="ride_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private Set<UserEntity> passengers;

    private double cost;
    private List<String> additionalDetails;


}
