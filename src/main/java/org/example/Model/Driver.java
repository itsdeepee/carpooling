package org.example.Model;



import lombok.*;
import org.example.Model.DTOs.RideDTO;
import org.example.Model.DTOs.UserDTO;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Driver {


    private Long userId;
    private String drivingLicense;
    private Set<RideDTO> driverRides;


}
