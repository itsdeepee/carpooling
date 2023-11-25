package org.example.Repository;


import org.example.Model.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    List<UserEntity> findByFirstName(String firstName);

    List<UserEntity> findByLastName(String lastName);

    List<UserEntity> findByFullNameContainingIgnoreCase(String partialFullName);
    List<UserEntity> findByFullNameContainingIgnoreCaseAndRole(String partialFullName,String role);



    boolean existsByEmail(String email);


}
