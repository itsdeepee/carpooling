package org.example.Model.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "location")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @NotNull
    private String text;
    private String address;
    @NotNull
    @Column(unique = true)
    private String fullPlaceName;
    @NotNull
    private List<Double> coordinates;


}
