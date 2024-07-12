package com.lamdo.service.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lamdo.dto.JwtAuthenticationResponse;
import com.lamdo.dto.RefreshTokenRequest;
import com.lamdo.dto.SignInRequest;
import com.lamdo.dto.SignUpRequest;
import com.lamdo.entity.Role;
import com.lamdo.entity.User;
import com.lamdo.repository.UserRepository;
import com.lamdo.service.AuthenticationService;
import com.lamdo.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder encoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final  JwtService jwtService;

	public User signUp(SignUpRequest request) {
		User user = new User();
		
		user.setEmail(request.getEmail());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setRole(Role.USER);
		user.setPassword(encoder.encode(request.getPassword()));
		return userRepository.save(user);
	}
	
	public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
		
		var user = userRepository.findByEmail(signInRequest.getEmail())
				.orElseThrow(()-> new IllegalArgumentException("Invalid Email or Password"));
		
		var jwt = jwtService.generateToken(user);
		
		System.out.println("jwt: " + jwt.toString());
		System.out.println("ROLE_user.name():  " + Role.USER.name());
		
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		
		JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
		
		jwtAuthenticationResponse.setToken(jwt);
		jwtAuthenticationResponse.setRefreshToken(refreshToken);
		
		return jwtAuthenticationResponse;
	}
	
	public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
		String userEmail =jwtService.extractUsername(refreshTokenRequest.getToken());
		
		User user = userRepository.findByEmail(userEmail).orElseThrow();
		
		if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
			var jwt = jwtService.generateToken(user);
			JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
			
			jwtAuthenticationResponse.setToken(jwt);
			jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
			
			return jwtAuthenticationResponse;
		}
		
		return null;
		
		
	}
}
