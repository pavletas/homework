package fmi.homework.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import fmi.homework.enums.Gender;
import fmi.homework.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterDto {

    @NotNull
    @Size(min = 2, max = 15)
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;

    @URL
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(min = 2, max = 512)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
}
