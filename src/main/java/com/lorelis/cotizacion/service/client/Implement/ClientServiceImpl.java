package com.lorelis.cotizacion.service.client.Implement;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.repository.client.ClientRepository;
import com.lorelis.cotizacion.service.client.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;


    // Convertir de DTO a entidad
    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setTypeDocument(dto.getTypeDocument());
        client.setDocumentNumber(dto.getDocumentNumber());
        client.setBusinessName(dto.getBusinessName());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setEmail(dto.getEmail());
        client.setEnabled(dto.getEnabled());
        return client;
    }

    // Convertir de entidad a DTO
    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setTypeDocument(client.getTypeDocument());
        dto.setDocumentNumber(client.getDocumentNumber());
        dto.setBusinessName(client.getBusinessName());
        dto.setPhoneNumber(client.getPhoneNumber());
        dto.setEmail(client.getEmail());
        dto.setEnabled(client.getEnabled());
        return dto;
    }

    @Override
    public void saveClient(ClientDTO clienteDTO) {
        // Buscar cliente existente por número de documento
        Client existingClient = clientRepository.findByDocumentNumber(clienteDTO.getDocumentNumber());

        if (existingClient != null) {
            if (existingClient.getEnabled()) {
                // Si ya existe y está habilitado, lanzar error
                throw new RuntimeException("Ya existe un cliente con ese número de documento.");
            } else {
                // Si existe pero está deshabilitado, lo reactivamos y actualizamos sus datos
                existingClient.setFirstName(clienteDTO.getFirstName());
                existingClient.setLastName(clienteDTO.getLastName());
                existingClient.setBusinessName(clienteDTO.getBusinessName());
                existingClient.setEmail(clienteDTO.getEmail());
                existingClient.setPhoneNumber(clienteDTO.getPhoneNumber());
                existingClient.setTypeDocument(clienteDTO.getTypeDocument());
                existingClient.setEnabled(true);

                clientRepository.save(existingClient);
                return;
            }
        }

        // Si no existe, guardar nuevo
        Client nuevoCliente = convertToEntity(clienteDTO);
        nuevoCliente.setEnabled(true); // Asegurarse que se guarde habilitado
        clientRepository.save(nuevoCliente);
    }


    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        return clientOpt.map(this::convertToDTO).orElse(null);
    }

    @Override
    public void updateClient(ClientDTO clienteDTO) {
        if (clienteDTO.getId() == null) return;

        Optional<Client> clientOpt = clientRepository.findById(clienteDTO.getId());
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();

            // ✅ Verificar si ya existe otro cliente con ese número de documento
            Client existingClient = clientRepository.findByDocumentNumber(clienteDTO.getDocumentNumber());
            if (existingClient != null && !existingClient.getId().equals(clienteDTO.getId())) {
                throw new RuntimeException("Ya existe un cliente con ese número de documento");
            }

            client.setTypeDocument(clienteDTO.getTypeDocument());
            client.setDocumentNumber(clienteDTO.getDocumentNumber());
            client.setPhoneNumber(clienteDTO.getPhoneNumber());
            client.setEmail(clienteDTO.getEmail());

            if ("DNI".equalsIgnoreCase(clienteDTO.getTypeDocument())) {
                client.setFirstName(clienteDTO.getFirstName());
                client.setLastName(clienteDTO.getLastName());
                client.setBusinessName("-"); // limpia campo RUC
            } else if ("RUC".equalsIgnoreCase(clienteDTO.getTypeDocument())) {
                client.setBusinessName(clienteDTO.getBusinessName());
                client.setFirstName("-"); // limpia campos DNI
                client.setLastName("-");
            }

            clientRepository.save(client);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> deleteClient(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Client> opt = clientRepository.findById(id);

        if (opt.isPresent()) {
            Client client = opt.get();
            client.setEnabled(false);
            clientRepository.save(client);
            respuesta.put("mensaje", "Cliente deshabilitado correctamente");
            respuesta.put("status", HttpStatus.NO_CONTENT);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuesta);
        } else {
            respuesta.put("mensaje", "Cliente no encontrado");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
    @Override
    public ClientDTO getClientByDocumentNumber(String documentNumber) {
        Client client= clientRepository.findByDocumentNumber(documentNumber);
        if(client== null || !client.getEnabled()){
            return null;
        }
        return convertToDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClientsEnabled() {
        return clientRepository.findByEnabledTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
