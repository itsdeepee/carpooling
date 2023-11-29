package org.example;

import org.example.Model.Entities.UserEntity;
import org.example.Repository.UserRepository;
import org.example.Service.DriverService;
import org.example.Service.RideService;
import org.example.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseLoader  implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final DriverService driverService;
    private final RideService rideService;

    public DatabaseLoader(
            UserService userService,
            DriverService driverService,
            RideService rideService,
            UserRepository userRepository
                          ){
        this.userService=userService;
        this.driverService=driverService;
        this.rideService=rideService;
        this.userRepository=userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        UserEntity user1=new UserEntity();
        user1.setFirstName("Sophie");
        user1.setLastName("Bennett");
        user1.setFullName("Sophie Bennett");
        user1.setEmail("sophie.bennett@email.com");
        user1.setPhoneNo("+40 712 345 678");
        user1.setBirthdate(LocalDate.of(1989,11,16));
        user1.setPassword("2fssADSA");
        user1.setRole("PASSENGER");

        UserEntity user2=new UserEntity();
        user2.setFirstName("Luis");
        user2.setLastName("Andersson");
        user2.setFullName("Luis Andersson");
        user2.setEmail("luis.andersson@email.com");
        user2.setPhoneNo("+40 731 234 567");
        user2.setBirthdate(LocalDate.of(1989,5,2));
        user2.setPassword("2d3r2ded");
        user2.setRole("PASSENGER");

        UserEntity user3=new UserEntity();
        user3.setFirstName("Ana");
        user3.setLastName("Dumitrescu");
        user3.setFullName("Ana Dumitrescu");
        user3.setEmail("ana.dumitrescu@email.com");
        user3.setPhoneNo("+40 770 987 654");
        user3.setBirthdate(LocalDate.of(1980,5,6));
        user3.setPassword("12d3S4");
        user3.setRole("PASSENGER");

        UserEntity user4=new UserEntity();
        user4.setFirstName("Marius");
        user4.setLastName("Ionescu");
        user4.setFullName("Marius Ionescu");
        user4.setEmail("marius.ionescu@email.com");
        user4.setPhoneNo("+40 742 876 543");
        user4.setBirthdate(LocalDate.of(1992,12,23));
        user4.setPassword("5f23f34");
        user4.setRole("PASSENGER");


     //   userRepository.saveAll(Arrays.asList(user1, user2,user3,user4));
    }
}
