package com.lorelis.cotizacion.service.client.Implement;

import com.lorelis.cotizacion.dto.client.ClienteDTO;
import com.lorelis.cotizacion.repository.client.ClientRepository;
import com.lorelis.cotizacion.service.client.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void saveClient(ClienteDTO clienteDTO) {


    }

    @Override
    public List<ClienteDTO> getAllClients() {
        return List.of();
    }

    @Override
    public ClienteDTO getClientById(Long id) {
        return null;
    }

    @Override
    public void updateClient(ClienteDTO clienteDTO) {

    }

    @Override
    public void deleteClient(Long id) {

    }
}
