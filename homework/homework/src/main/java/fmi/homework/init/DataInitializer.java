package fmi.homework.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import fmi.homework.dto.UserRegisterDto;
import fmi.homework.enums.Gender;
import fmi.homework.enums.Role;
import fmi.homework.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final List<UserRegisterDto> USERS = List.of(
            new UserRegisterDto("ivan", "123EDCed@5",
                    "https://t4.ftcdn.net/jpg/02/70/22/85/360_F_270228529_iDayZ2Dl4ZeDClKl7ZnLgzN5HRIvlGlK.jpg",
                    Gender.FEMALE, "some content", Role.ADMIN));

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userService.getUsers().isEmpty()) {
            USERS.forEach(userService::registerUser);
        }
    }
}