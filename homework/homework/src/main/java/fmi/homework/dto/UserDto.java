package fmi.homework.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import fmi.homework.enums.AccountStatus;
import fmi.homework.enums.Gender;
import fmi.homework.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 2, max = 15)
    private String username;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}
