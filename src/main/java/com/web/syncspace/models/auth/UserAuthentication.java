package com.web.syncspace.models.auth;


import com.web.syncspace.enums.Role;
import com.web.syncspace.models.authorities.Admin;
import com.web.syncspace.models.authorities.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "user_auth")
public class UserAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id", nullable = false)
    private Long authId;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "userAuthentication")
    private Users users;

    @OneToOne(mappedBy = "userAuthentication")
    private Admin admin;

}
