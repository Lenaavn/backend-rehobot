package com.reho.service.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reho.config.JwtService;
import com.reho.persistence.entities.Usuario;
import com.reho.persistence.entities.enums.Rol;
import com.reho.persistence.repository.UsuarioRepository;
import com.reho.web.models.AuthResponse;
import com.reho.web.models.AuthenticationRequest;
import com.reho.web.models.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String nombreCompleto = request.getNombre() + " " + request.getApellidos(); 

        var usuario = Usuario.builder()
                .nombre(nombreCompleto)
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .telefono(request.getTelefono())
                .rol(Rol.USER)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        var accessToken = jwtService.generateAccessToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), 
                        request.getContrasena()
                )
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(usuario);
        var refreshToken = jwtService.generateRefreshToken(usuario);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public boolean existeUsuarioPorTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono).isPresent();
    }

    public boolean verificarContrasena(String email, String contrasena) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasena()))
                .orElse(false);
    }
}
