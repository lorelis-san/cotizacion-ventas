package com.lorelis.cotizacion.service.usuario;

import com.lorelis.cotizacion.dto.users.UserDTO;
import com.lorelis.cotizacion.model.usuario.Usuario;

public interface UsuarioService {
    Usuario registrarUsuario(UserDTO dto);
    Usuario buscarPorEmail(String email);
}
