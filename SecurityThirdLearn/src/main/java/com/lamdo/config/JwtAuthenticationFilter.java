package com.lamdo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lamdo.service.JwtService;
import com.lamdo.service.UserService;

import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
//Chỉ thực thi một lần nếu dùng OncePerRequestFilter
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	
	private final UserService userSerivce; 
	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if(StringUtils.isEmpty(authHeader) || StringUtils.startsWith(authHeader, "Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		System.out.println("Print jwt: " + jwt.toString());
		userEmail = jwtService.extractUsername(jwt);
		System.out.println("User email: " + userEmail);
		if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails =userSerivce.userDetailsService().loadUserByUsername(userEmail);
			if(jwtService.isTokenValid(jwt, userDetails)) {
//				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				
//				UsernamePasswordAuthenticationToken authentication = 
//						new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				
////				securityContext.setAuthentication(authentication);
//				
////				SecurityContextHolder.setContext(securityContext);
				
				UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		        SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		doFilter(request, response,filterChain);
	}

}
