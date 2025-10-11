package com.web.syncspace.services.authorites.users;

import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.dto.register.UsersRegisterDTO;
import com.web.syncspace.models.authorities.Users;
import com.web.syncspace.repositories.authorities.UsersRepository;
import com.web.syncspace.services.auth.UserAuthenticationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserAuthenticationFactory userAuthenticationFactory;
    private final UsersRepository usersRepository;

    @Override
    public RegisterResponseDTO registerUser(UsersRegisterDTO usersRegisterDTO) {
        Users users = Users.builder()
                .name(usersRegisterDTO.getName())
                .userAuthentication(userAuthenticationFactory.saveUser(usersRegisterDTO))
                .build();

        usersRepository.save(users);

        return RegisterResponseDTO.builder()
                .name(users.getName())
                .email(users.getUserAuthentication().getEmail())
                .mobileNumber(users.getUserAuthentication().getMobileNumber())
                .profileImageUrl(users.getProfileImageUrl())
                .role(users.getUserAuthentication().getRole())
                .dateOfJoining(users.getDateOfJoining())
                .build();

    }
}
