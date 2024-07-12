package com.lamdo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamdo.dto.JwtAuthenticationResponse;
import com.lamdo.dto.RefreshTokenRequest;
import com.lamdo.dto.SignInRequest;
import com.lamdo.dto.SignUpRequest;
import com.lamdo.entity.User;
import com.lamdo.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest){
		return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest){
		System.out.println("Dang nhap voi user: " + signInRequest.toString());
		return ResponseEntity.ok(authenticationService.signIn(signInRequest));
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest tokenRequest){
		return ResponseEntity.ok(authenticationService.refreshToken(tokenRequest));
	}
}
