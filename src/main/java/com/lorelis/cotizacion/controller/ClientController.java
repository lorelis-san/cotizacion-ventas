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


    // Mostrar formulario para nuevo cliente
    @GetMapping("/nuevoCliente")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new ClientDTO());
        return "cliente/clienteAgregar"; // templates/clientes/formulario.html
    }

    // Guardar nuevo cliente
    @PostMapping("/clients/guardar")
    public String guardarCliente(@ModelAttribute("cliente") ClientDTO cliente) {

            clientService.saveClient(cliente);
            return "redirect:/clients"; // Redirección a la lista de clientes

    }

    // Mostrar formulario para editar cliente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        ClientDTO cliente = clientService.getClientById(id);
        model.addAttribute("cliente", cliente);
        return "clientes/formulario"; // Reutilizamos el mismo formulario
    }

    // Actualizar cliente
    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute("cliente") ClientDTO cliente) {
        clientService.updateClient(cliente);
        return "redirect:/clientes";
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clientes";
    }
}