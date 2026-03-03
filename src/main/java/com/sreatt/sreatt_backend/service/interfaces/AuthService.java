package com.sreatt.sreatt_backend.service.interfaces;

import com.sreatt.sreatt_backend.dto.SignupDto;
import com.sreatt.sreatt_backend.dto.UserDto;

public interface AuthService {
	String[] login(String email, String password);
    UserDto signup(SignupDto signupDto);
}