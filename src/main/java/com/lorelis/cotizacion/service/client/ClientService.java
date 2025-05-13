package com.lorelis.cotizacion.service.client;

import com.lorelis.cotizacion.dto.client.ClienteDTO;
import com.lorelis.cotizacion.model.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lorelis.cotizacion.repository.client.ClientRepository;

import java.util.List;

@Service
public interface ClientService {
    void saveClient(ClienteDTO clienteDTO);
    List<ClienteDTO> getAllClients();
    ClienteDTO getClientById(Long id);
    void updateClient(ClienteDTO clienteDTO);
    void deleteClient(Long id); //inhabilitar


}
