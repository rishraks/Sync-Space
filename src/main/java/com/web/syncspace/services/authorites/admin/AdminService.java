package com.web.syncspace.services.authorites.admin;

import com.web.syncspace.dto.register.AdminRegisterDTO;
import com.web.syncspace.dto.register.RegisterResponseDTO;

public interface AdminService {

    RegisterResponseDTO registerAdmin(AdminRegisterDTO adminRegisterDTO);
}
