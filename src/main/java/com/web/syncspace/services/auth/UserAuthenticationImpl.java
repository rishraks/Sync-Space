package com.web.syncspace.services.auth;

import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.exceptions.others.DuplicateResourceException;
import com.web.syncspace.repositories.auth.UserAuthenticationRepository;
import com.web.syncspace.services.authorites.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UsersService usersService;

    @Override
    public RegisterResponseDTO registerUser(UsersRegisterDTO usersRegisterDTO) {
        if (userAuthenticationRepository.findByEmail(usersRegisterDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User with email " + usersRegisterDTO.getEmail() + " already exists.");
        }
        if (userAuthenticationRepository.findByMobileNumber(usersRegisterDTO.getMobileNumber()).isPresent()) {
            throw new DuplicateResourceException("User with mobile number " + usersRegisterDTO.getMobileNumber() + " already exists.");
        }
        return usersService.registerUser(usersRegisterDTO);
    }


}
