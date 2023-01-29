package fmi.homework.services;

import java.util.List;

import fmi.homework.dto.UserDto;
import fmi.homework.dto.UserRegisterDto;
import fmi.homework.models.User;

public interface UserService {

    List<User> getUsers();

    User getById(Long userId);

    User getByUsername(String username);

    User registerUser(UserRegisterDto userRegisterDto);

    User updateUserById(Long userId, UserDto userDto);

    void deleteUserById(Long userId);
}
