package org.example.Model.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@Table(name = "rides",uniqueConstraints = {@UniqueConstraint(columnNames = {"driver_id","date_time_of_ride"})})
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long rideId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_location_id", nullable = false) // Assuming a location ID
    private LocationEntity departureLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_location_id", nullable = false) // Assuming a location ID
    private LocationEntity destinationLocation;

    @Column(name = "date_time_of_ride", nullable = false)
    private LocalDateTime dateTimeOfRide;

    @Column(name = "available_seats", nullable = false)
    @Min(value = 0, message = "Available seats should be a positive number or zero")
    private int availableSeats;

    @NotBlank(message = "Status cannot be blank")
    @Column(name = "ride_status", nullable = false, length = 30)
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ride_id", nullable = false)
    @JsonManagedReference
    @ToString.Exclude
    private Set<RideRequestEntity> rideRequestEntities;


    @Column(name = "cost")
    private double cost;
    @ElementCollection

    @CollectionTable(name = "ride_additional_details", joinColumns = @JoinColumn(name = "ride_id"))
    @Column(name = "additional_detail") // Adjust length as needed
    private List<String> additionalDetails;


}
