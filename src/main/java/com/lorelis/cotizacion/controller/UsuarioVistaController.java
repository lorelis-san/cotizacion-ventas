package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.users.UserDTO;
import com.lorelis.cotizacion.service.usuario.RolService;
import com.lorelis.cotizacion.service.usuario.SedeService;
import com.lorelis.cotizacion.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioVistaController {

    private final UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private SedeService sedeService;

    @GetMapping("/verRegistrar")
    public String mostrarFormularioRegistrar(Model model) {
        model.addAttribute("usuario", new UserDTO()); // o UserDTO si prefieres
        model.addAttribute("roles", rolService.listarRoles());
        model.addAttribute("sedes", sedeService.listarSedes());
        return "usuario/register";
    }
    @PostMapping("/verRegistrar")
    public String procesarRegistro(@ModelAttribute("usuario") UserDTO userDTO) {
        usuarioService.registrarUsuario(userDTO);
        return "redirect:/usuarios/verRegistrar?exito";
    }
}