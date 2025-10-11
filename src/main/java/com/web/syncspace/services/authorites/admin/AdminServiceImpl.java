package com.web.syncspace.services.authorites.admin;

import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;
import com.web.syncspace.models.authorities.Admin;
import com.web.syncspace.repositories.authorities.AdminRepository;
import com.web.syncspace.services.auth.UserAuthenticationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserAuthenticationFactory userAuthenticationFactory;
    private final AdminRepository adminRepository;

    @Override
    public RegisterResponseDTO registerAdmin(AdminRegisterDTO adminRegisterDTO) {
        Admin admin = Admin.builder()
                .name(adminRegisterDTO.getName())
                .userAuthentication(userAuthenticationFactory.saveAdmin(adminRegisterDTO))
                .build();

        adminRepository.save(admin);

        return RegisterResponseDTO.builder()
                .name(admin.getName())
                .email(admin.getUserAuthentication().getEmail())
                .mobileNumber(admin.getUserAuthentication().getMobileNumber())
                .profileImageUrl(admin.getProfileImageUrl())
                .role(admin.getUserAuthentication().getRole())
                .dateOfJoining(admin.getDateOfJoining())
                .build();

    }
}
