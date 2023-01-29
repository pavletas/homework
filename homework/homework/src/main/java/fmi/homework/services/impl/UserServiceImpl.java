package fmi.homework.services.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fmi.homework.dto.UserDto;
import fmi.homework.dto.UserRegisterDto;
import fmi.homework.enums.Gender;
import fmi.homework.enums.Role;
import fmi.homework.exceptions.EntityNotFoundException;
import fmi.homework.exceptions.InvalidEntityDataException;
import fmi.homework.models.User;
import fmi.homework.repositories.RecipeRepository;
import fmi.homework.repositories.UserRepository;
import fmi.homework.services.UserService;
import fmi.homework.utils.JwtUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String USER_ENTITY_NAME = "User";

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = this.getByUsername(username);
        return jwtUtils.getUserDetails(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY_NAME, "username", username));
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            throw new InvalidEntityDataException(String.format("User with username [%s] already exists!",
                    userRegisterDto.getUsername()));
        }

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setContent(userRegisterDto.getContent());
        user.setGender(userRegisterDto.getGender());
        setImageUrl(userRegisterDto.getImageUrl(), user);
        Role role = userRegisterDto.getRole() == null ? Role.USER : userRegisterDto.getRole();
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserById(Long userId, UserDto userDto) {
        User user = getById(userId);

        if (userDto.getRole().equals(Role.ADMIN)) {
            User loggedUser = getByUsername(jwtUtils.getCurrentUsername());

            if (user.getId().equals(loggedUser.getId())) {
                throw new InvalidEntityDataException(String.format("Role: '%s' is invalid. "
                        + "You can self-register only in 'USER' role.", userDto.getRole()));
            }
        }

        user.setUsername(userDto.getUsername());
        user.setContent(userDto.getContent());
        user.setGender(userDto.getGender());
        user.setRole(userDto.getRole());
        user.setAccountStatus(userDto.getAccountStatus());
        setImageUrl(userDto.getImageUrl(), user);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User foundUser = getById(userId);
        userRepository.delete(foundUser);
    }

    private void setImageUrl(String imageUrl, User user) {
        if (imageUrl == null) {
            imageUrl = user.getGender().equals(Gender.FEMALE) ?
                    "https://t4.ftcdn.net/jpg/02/70/22/85/360_F_270228529_iDayZ2Dl4ZeDClKl7ZnLgzN5HRIvlGlK.jpg" :
                    "https://cdn5.vectorstock.com/i/1000x1000/45/79/male-avatar-profile-picture-silhouette-light-vector-4684579.jpg";
        }

        user.setImageUrl(imageUrl);
    }
}
