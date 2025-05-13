package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*") // Allows requests from any frontend (for example: React, Angular, etc.)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<String> createClient(@RequestBody ClientDTO clientDTO) {
        clientService.saveClient(clientDTO);
        return ResponseEntity.ok("Client created successfully");
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientDTO.setId(id); // Ensure the ID is correctly set
        clientService.updateClient(clientDTO);
        return ResponseEntity.ok("Client updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id); // You can implement soft delete here
        return ResponseEntity.ok("Client deleted successfully");
    }
}
