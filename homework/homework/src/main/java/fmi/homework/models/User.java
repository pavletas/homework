package fmi.homework.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fmi.homework.enums.AccountStatus;
import fmi.homework.enums.Gender;
import fmi.homework.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotNull
    @Size(min = 2, max = 15)
    @Column(name = "username", unique = true)
    private String username;

    @NonNull
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    //  @URL
    @Column(name = "image_url")
    private String imageUrl;

    @NonNull
    @NotNull
    @Size(min = 2, max = 512)
    @Column(name = "content")
    private String content;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;

    @NonNull
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Recipe> recipes = new HashSet<>();

    @CreationTimestamp
    @Column(name = "row_created", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "row_modified")
    private LocalDateTime modified;
}
