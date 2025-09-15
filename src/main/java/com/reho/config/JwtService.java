package com.reho.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.reho.persistence.entities.Usuario;
import com.reho.persistence.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private static final String SECRET_KEY = "k/XQCmS3xR5VJd23DjTtthpbDuL0mZ2mL6WUxhjkYOA=";
	private final UsuarioRepository usuarioRepository;

	// GENERAR ACCESS TOKEN 
	public String generateAccessToken(UserDetails userDetails) {
		Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("id", usuario.getId());
		extraClaims.put("nombre", usuario.getNombre());
		extraClaims.put("email", usuario.getEmail());
		extraClaims.put("rol", usuario.getRol());

		return buildToken(extraClaims, userDetails.getUsername(), 1000 * 60 * 60 * 24 * 7); // 7 días
	}

	// GENERAR REFRESH TOKEN 
	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails.getUsername(), 1000 * 60 * 60 * 24 * 60); // 60 días
	}

	// Método privado común para crear tokens
	private String buildToken(Map<String, Object> claims, String subject, long expirationMs) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	// Validar el Refresh Token
	public boolean validateRefreshToken(String token) {
	    try {
	        return !isTokenExpired(token); // Solo comprobar que no ha expirado
	    } catch (Exception e) {
	        return false;
	    }
	}


	public String getUserName(String token) {
		return getClaim(token, Claims::getSubject);
	}

	private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.setAllowedClockSkewSeconds(60)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = getUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}

	private Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}
}
