package org.example.Service;

import org.example.Model.DTOs.RideDTO;
import org.example.Model.Driver;
import org.example.Model.Location;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {


    List<RideDTO> rideDTOS=new ArrayList<>();


    public RideService(){

    }

    public List<RideDTO> findAll(){return rideDTOS;}

    public  List<RideDTO> findAllByDate(LocalDate dateOfDeparture){
        return null;
    }

    public  List<RideDTO> findAllByDateAndTime(LocalDate dateOfDeparture,LocalTime timeOfDeparture){
        return null;
    }

    public List<RideDTO> findAllByDriver(Driver driver){
        return null;
    }

    public boolean save(RideDTO rideDTO){
        //check to see if driver exists.
//        Driver driver=driverRepository.findDriverById(rideDTO.getDriverId());



        return rideDTOS.add(rideDTO);
    }

    public void update(RideDTO rideDTO){
        //
    }

    public boolean delete(RideDTO rideDTO){
        return rideDTOS.remove(rideDTO);
    }





}
