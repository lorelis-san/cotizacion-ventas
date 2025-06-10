package com.lorelis.cotizacion.service.client;

import com.lorelis.cotizacion.dto.client.ClientDTO;

import java.util.List;

public interface ClientService {
    void saveClient(ClientDTO clienteDTO);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    void updateClient(ClientDTO clienteDTO);
    void deleteClient(Long id); //inhabilitar
    ClientDTO getClientByDocumentNumber(String documentNumber);

}
