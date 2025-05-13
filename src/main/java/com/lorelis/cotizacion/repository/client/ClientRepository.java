package com.lorelis.cotizacion.repository.client;

import com.lorelis.cotizacion.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findbyDocumentNumber(String documentNumber);
}
