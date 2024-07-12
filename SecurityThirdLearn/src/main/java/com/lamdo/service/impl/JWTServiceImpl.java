package com.lamdo.service.impl;


import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lamdo.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTServiceImpl implements JwtService{
	
	@Value("${jwt.key}")
    private String JWT_SECRET;
	
//	Generate Key sẽ tham khảo nhiều nguồn
	@Override
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000  * 60 * 24))
				.signWith(getSigninKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	@Override
	public String generateRefreshToken(Map<String, Object>extraClaims ,UserDetails userDetails) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(getSigninKey(),SignatureAlgorithm.HS256)
				.compact();
	}

// Giai ma token voi cac ham claimsRsolver	
	private<T> T extractClaim(String token, Function<Claims, T>claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);

	}
	
//	Lay ra username qua token duoc cap
	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
//	Signinkey duoc co dinh tai day va co the get bat cu luc nao
	public SecretKey getSigninKey() {
		SecretKey key1 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
		System.out.println("Mat khau bi mat"+JWT_SECRET);
		return key1;
	}
	
//	Giai ma ra cac claims tu token
//	Claims don gian la nguoc lai cua build(); build ghep vao trong 1 thi claims chia ra so nhieu
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJwt(token).getBody();
	}
	
//	Kiểm tra token valid không bằng cách so sánh username và hạn sử dụng của token này
	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
	

}
