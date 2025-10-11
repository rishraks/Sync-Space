package com.web.syncspace.services.auth;

import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.exceptions.others.DuplicateResourceException;
import com.web.syncspace.repositories.auth.UserAuthenticationRepository;
import com.web.syncspace.services.authorites.admin.AdminService;
import com.web.syncspace.services.authorites.users.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UsersService usersService;
    private final AdminService adminService;

    @Override
    public RegisterResponseDTO registerUser(UsersRegisterDTO usersRegisterDTO) {
        validateUsers(usersRegisterDTO.getEmail(), usersRegisterDTO.getMobileNumber());
        return usersService.registerUser(usersRegisterDTO);
    }

    @Override
    public RegisterResponseDTO registerAdmin(AdminRegisterDTO adminRegisterDTO) {
        validateUsers(adminRegisterDTO.getEmail(), adminRegisterDTO.getMobileNumber());
        return adminService.registerAdmin(adminRegisterDTO);
    }


    private void validateUsers(String email, String mobileNumber) {
        if (userAuthenticationRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("User with email " + email + " already exists.");
        }
        if (userAuthenticationRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new DuplicateResourceException("User with mobile number " + mobileNumber + " already exists.");
        }
    }


}
