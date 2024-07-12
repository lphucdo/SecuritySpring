package com.lamdo.service;

import com.lamdo.dto.JwtAuthenticationResponse;
import com.lamdo.dto.RefreshTokenRequest;
import com.lamdo.dto.SignInRequest;
import com.lamdo.dto.SignUpRequest;
import com.lamdo.entity.User;

public interface AuthenticationService {

	
	User signUp(SignUpRequest request);
	
	JwtAuthenticationResponse signIn(SignInRequest signInRequest);
	
	JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
