package org.example.Repository.Users;


import org.example.Model.Entities.UserEntity;

import java.util.List;

public interface UserRepositoryInterface {

    public UserEntity createUser(UserEntity userEntity);

    public List<UserEntity> getAllUsers();
}
