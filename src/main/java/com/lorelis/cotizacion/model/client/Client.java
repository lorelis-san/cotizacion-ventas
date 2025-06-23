package com.lorelis.cotizacion.model.client;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "type_document", nullable = false)
    private String typeDocument;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;  // Unique field for document (DNI or RUC)

    @Column(name = "business_name", nullable = true)
    private String businessName;  // Only applies if it's a RUC

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;
    private Boolean enabled = true;


}
