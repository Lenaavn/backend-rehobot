package com.reho.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reho.config.JwtService;
import com.reho.persistence.repository.UsuarioRepository;
import com.reho.service.authentication.AuthService;
import com.reho.service.authentication.AuthServiceImpl;
import com.reho.web.models.AuthResponse;
import com.reho.web.models.AuthenticationRequest;
import com.reho.web.models.RegisterRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private AuthServiceImpl authServiceImpl;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
	    if (authServiceImpl.existeUsuarioPorEmail(request.getEmail())) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
				.mensaje("El email ya está en uso.")
				.build());
	    }

	    if (authServiceImpl.existeUsuarioPorTelefono(request.getTelefono())) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
				.mensaje("El teléfono ya está en uso.")
				.build());
	    }

	    authService.register(request);

	    var usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
	    String accessToken = jwtService.generateAccessToken(usuario);
	    String refreshToken = jwtService.generateRefreshToken(usuario);

	    return ResponseEntity.ok(AuthResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build());
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticationRequest request) {
	    if (request.getEmail() == null || request.getEmail().isEmpty()) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("El email es obligatorio.")
	            .build());
	    }

	    if (request.getContrasena() == null || request.getContrasena().isEmpty()) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("La contraseña es obligatoria.")
	            .build());
	    }

	    if (!authServiceImpl.existeUsuarioPorEmail(request.getEmail())) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("El email es incorrecto.")
	            .build());
	    }

	    if (!authServiceImpl.verificarContrasena(request.getEmail(), request.getContrasena())) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("La contraseña es incorrecta.")
	            .build());
	    }

	    var usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

	    if (!usuario.isActivo()) {
	        return ResponseEntity.status(403).body(AuthResponse.builder()
	            .mensaje("El usuario está desactivado.")
	            .build());
	    }

	    String accessToken = jwtService.generateAccessToken(usuario);
	    String refreshToken = jwtService.generateRefreshToken(usuario);

	    return ResponseEntity.ok(AuthResponse.builder()
	        .accessToken(accessToken)
	        .refreshToken(refreshToken)
	        .mensaje("Inicio sesión correctamente.")
	        .build());
	}


	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody AuthenticationRequest request) {
	    if (request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("El refresh token es obligatorio.")
	            .build());
	    }

	    String email = jwtService.getUserName(request.getRefreshToken());
	    var usuario = usuarioRepository.findByEmail(email).orElseThrow();

	    if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
	        return ResponseEntity.badRequest().body(AuthResponse.builder()
	            .mensaje("Refresh token inválido o expirado.")
	            .build());
	    }

	    // Generar un nuevo access token
	    String nuevoAccessToken = jwtService.generateAccessToken(usuario);

	    return ResponseEntity.ok(AuthResponse.builder()
	        .accessToken(nuevoAccessToken)
	        .refreshToken(request.getRefreshToken()) // Reutilizamos el refresh token existente
	        .build());
	}


}
