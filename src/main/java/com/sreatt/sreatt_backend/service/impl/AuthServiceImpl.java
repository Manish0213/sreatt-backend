package com.sreatt.sreatt_backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sreatt.sreatt_backend.constant.ErrorCodeEnum;
import com.sreatt.sreatt_backend.dto.SignupDto;
import com.sreatt.sreatt_backend.dto.UserDto;
import com.sreatt.sreatt_backend.entity.User;
import com.sreatt.sreatt_backend.entity.enums.Role;
import com.sreatt.sreatt_backend.exceptions.AuthException;
import com.sreatt.sreatt_backend.repository.UserRepository;
import com.sreatt.sreatt_backend.security.JwtService;
import com.sreatt.sreatt_backend.service.interfaces.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final AuthenticationManager authenticationManager;

	@Override
	public String[] login(String email, String password) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(email,password)
					);

			User user = (User) authentication.getPrincipal();
			String accessToken = jwtService.generateAccessToken(user);
			String role = user.getRole().name();
			return new String[]{accessToken, role};
			
		} catch (UsernameNotFoundException ex) {
			throw new AuthException(
	                "USER_NOT_FOUND",
	                "User not found with email",
	                HttpStatus.NOT_FOUND
	        );
	    } catch (BadCredentialsException ex) {
	        throw new AuthException(
	                "INVALID_CREDENTIALS",
	                "Invalid email or password",
	                HttpStatus.UNAUTHORIZED
	        );
	    } catch (Exception ex) {
	        throw new AuthException(
	                "AUTH_FAILED",
	                "Authentication failed",
	                HttpStatus.INTERNAL_SERVER_ERROR
	        );
	    }
	}

	public UserDto signup(SignupDto signupDto) {
		User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);
		
		if(user != null) {
			throw new AuthException(
					ErrorCodeEnum.USER_ALREADY_EXISTS.getErrorCode(),
					ErrorCodeEnum.USER_ALREADY_EXISTS.getErrorMessage(),
					HttpStatus.CONFLICT
					);
		}

		User mappedUser = modelMapper.map(signupDto, User.class);
		mappedUser.setRole(Role.USER);
		mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
		User savedUser = userRepository.save(mappedUser);

		return modelMapper.map(savedUser, UserDto.class);
	}
}
