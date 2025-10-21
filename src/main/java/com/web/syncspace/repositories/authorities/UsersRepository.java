package com.web.syncspace.repositories.authorities;

import com.web.syncspace.models.authorities.Users;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(@NotBlank(message = "Sender id is required") String senderUserName);
}
