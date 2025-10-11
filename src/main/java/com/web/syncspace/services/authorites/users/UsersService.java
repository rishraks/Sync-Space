package com.web.syncspace.services.authorites.users;


import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;

public interface UsersService {
    RegisterResponseDTO registerUser(UsersRegisterDTO usersRegisterDTO);
}
