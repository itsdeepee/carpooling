package org.example.Model;

import lombok.Getter;
import lombok.ToString;
import org.example.Model.DTOs.RideDTO;
import org.example.Model.DTOs.UserDTO;

import java.util.Set;

@Getter
@ToString
public class Passenger  {

    private Set<RideDTO> bookedRides;


}
