package fmi.homework.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fmi.homework.dto.UserDto;
import fmi.homework.dto.UserRegisterDto;
import fmi.homework.exceptions.InvalidEntityDataException;
import fmi.homework.models.User;
import fmi.homework.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public User getRecipeById(@PathVariable("userId") Long userId) {
        return userService.getById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRegisterDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        User createdUser = userService.registerUser(userDto);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                .build(createdUser.getId())).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,
            @Valid @RequestBody UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid user data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        User createdUser = userService.updateUserById(userId, userDto);
        return ResponseEntity.ok().body(createdUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
