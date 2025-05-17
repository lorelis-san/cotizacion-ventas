package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public String listarClientes(Model model) {
        List<ClientDTO> listaClientes = clientService.getAllClients();

        // Si no hay datos, pasar una lista vacía
        if (listaClientes == null) {
            listaClientes = new ArrayList<>();
        }
        model.addAttribute("listaClientes", listaClientes);
        return "cliente/index";
    }

    @PostMapping("/clients/guardar")
    public String guardarCliente(@ModelAttribute("cliente") ClientDTO cliente, Model model) {
        try {
            clientService.saveClient(cliente);
            return "redirect:/clients";
        } catch (RuntimeException e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("error", e.getMessage());
            return "cliente/clienteAgregar"; // Regresa al formulario
        }
    }

    // Mostrar formulario para nuevo cliente
    @GetMapping("/nuevoCliente")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new ClientDTO());
        return "cliente/clienteAgregar";
    }

    // Actualizar cliente
    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute ClientDTO clienteDTO) {
        clientService.updateClient(clienteDTO);
        return "redirect:/clients";
    }

    // Obtener cliente por ID para editar (API endpoint para AJAX)
    @GetMapping("/cliente/{id}")
    @ResponseBody
    public ClientDTO obtenerClientePorId(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}