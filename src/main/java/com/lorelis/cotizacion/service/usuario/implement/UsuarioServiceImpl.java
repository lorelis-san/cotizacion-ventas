package com.lorelis.cotizacion.service.usuario.implement;

import com.lorelis.cotizacion.dto.users.UserDTO;
import com.lorelis.cotizacion.model.usuario.Rol;
import com.lorelis.cotizacion.model.usuario.Sede;
import com.lorelis.cotizacion.model.usuario.Usuario;
import com.lorelis.cotizacion.repository.usuario.RolRepository;
import com.lorelis.cotizacion.repository.usuario.SedeRepository;
import com.lorelis.cotizacion.repository.usuario.UsuarioRepository;
import com.lorelis.cotizacion.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SedeRepository sedeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrarUsuario(UserDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Sede sede = sedeRepository.findById(dto.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        Usuario usuario = new Usuario();
        usuario.setName(dto.getName());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);
        usuario.setSede(sede);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
