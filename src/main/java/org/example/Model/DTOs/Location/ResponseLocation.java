package org.example.Model.DTOs.Location;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseLocation {

    private String text;
    private String address;
    private String fullPlaceName;
    private List<Double> coordinates;

}
