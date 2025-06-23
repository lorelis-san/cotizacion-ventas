package com.lorelis.cotizacion.repository.client;

import com.lorelis.cotizacion.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByDocumentNumber(String documentNumber);
    List<Client> findByEnabledTrue();
}
