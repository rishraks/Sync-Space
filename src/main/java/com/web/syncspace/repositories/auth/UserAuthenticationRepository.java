package com.web.syncspace.repositories.auth;

import com.web.syncspace.models.auth.UserAuthentication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByEmail(String userEmail);

    Optional<UserAuthentication> findByMobileNumber(@NotBlank(message = "Mobile number is required") @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid mobile number format") String mobileNumber);
}
