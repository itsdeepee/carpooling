package org.example.Model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Location {

    private String addressName;
    private double longitude;
    private double latitude;


}
