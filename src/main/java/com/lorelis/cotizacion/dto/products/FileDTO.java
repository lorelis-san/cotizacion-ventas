package com.lorelis.cotizacion.dto.products;

import com.lorelis.cotizacion.model.quote.Quote;
import jakarta.persistence.*;
@Entity
public class FileDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quote quotation;

    private String fileName;
    private String fileUrl;

    // Getters y setters
}
