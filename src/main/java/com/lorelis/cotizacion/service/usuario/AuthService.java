package com.lorelis.cotizacion.service.usuario;

import com.lorelis.cotizacion.dto.auth.LoginRequestDTO;
import com.lorelis.cotizacion.dto.auth.LoginResponseDTO;
import com.lorelis.cotizacion.model.usuario.Usuario;
import com.lorelis.cotizacion.repository.usuario.UsuarioRepository;
import com.lorelis.cotizacion.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRol().getName());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().getName());
        response.setSede(user.getSede().getName()); // o el campo correspondiente

        return response;
    }
}
