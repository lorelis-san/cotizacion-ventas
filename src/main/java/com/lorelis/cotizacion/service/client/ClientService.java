package com.lorelis.cotizacion.service.client;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ClientService {
    void saveClient(ClientDTO clienteDTO);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    void updateClient(ClientDTO clienteDTO);
    ResponseEntity<Map<String, Object>> deleteClient(Long id); //inhabilitar
    ClientDTO getClientByDocumentNumber(String documentNumber);
    public List<ClientDTO> getAllClientsEnabled();

}
