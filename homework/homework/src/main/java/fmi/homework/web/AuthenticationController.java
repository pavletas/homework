package fmi.homework.web;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fmi.homework.dto.AuthenticationRequest;
import fmi.homework.dto.UserRegisterDto;
import fmi.homework.enums.Role;
import fmi.homework.exceptions.EntityNotFoundException;
import fmi.homework.exceptions.InvalidEntityDataException;
import fmi.homework.models.User;
import fmi.homework.services.UserService;
import fmi.homework.utils.JwtUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthenticationRequest authenticationRequest,
            Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        UserDetails user;

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(401).body(exception.getMessage());
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(404).body(exception.getMessage());
        }

        return ResponseEntity.ok(jwtUtils.generateToken(user));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDto userRegisterDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        User user;

        try {
            if (!userRegisterDto.getRole().equals(Role.USER)) {
                throw new InvalidEntityDataException(String.format("Role: '%s' is invalid. "
                        + "You can self-register only in 'USER' role.", userRegisterDto.getRole()));
            }

            user = userService.registerUser(userRegisterDto);
        } catch (InvalidEntityDataException exception) {
            return ResponseEntity.status(409).body(exception.getMessage());
        }

        return ResponseEntity.ok(jwtUtils.generateToken(user));
    }
}
