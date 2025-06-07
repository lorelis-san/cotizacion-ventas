package com.lorelis.cotizacion.service.client.Implement;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.repository.client.ClientRepository;
import com.lorelis.cotizacion.service.client.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return dto;
    }

    @Override
    public void saveClient(ClientDTO clienteDTO) {
        // Buscar cliente con el mismo número de documento
        Client existingClient = clientRepository.findByDocumentNumber(clienteDTO.getDocumentNumber());

        if (existingClient != null) {
            throw new RuntimeException("Ya existe un cliente con ese número de documento");
        }
        clientRepository.save(convertToEntity(clienteDTO));
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
    public void deleteClient(Long id) {
        // Puedes cambiar esto a una lógica de "inhabilitar" si deseas usar un campo booleano "activo"
        clientRepository.deleteById(id);
    }
}
