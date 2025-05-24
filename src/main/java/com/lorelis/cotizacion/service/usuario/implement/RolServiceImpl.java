package com.lorelis.cotizacion.service.usuario.implement;


import com.lorelis.cotizacion.model.usuario.Rol;
import com.lorelis.cotizacion.service.usuario.RolService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    @Override
    public List<Rol> listarRoles() {
        List<Rol> roles = new ArrayList<>();
        roles.add(new Rol(1L, "Administrador"));
        roles.add(new Rol(2L, "Vendedor"));
        return roles;
    }
}