package com.lorelis.cotizacion.model.quote;

import com.lorelis.cotizacion.model.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id")
    private Long id;

    @Column(name = "nro_quote", nullable = false, unique = true)
    private String nroQuote;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "state", nullable = false)
    private String state;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuoteDetails> QuoteDetails;


    // Relación con el contador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_counter", nullable = false)
    private CounterQuote counterQuote;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        updateDate = LocalDateTime.now();

        // Formatear el número de cotización con ceros a la izquierda
        this.nroQuote = generateQuoteNumber();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = LocalDateTime.now();
    }

    // Método para generar el número de cotización
    private String generateQuoteNumber() {
        Long counter = counterQuote.incrementCounter();
        return String.format("N° %09d", counter);
    }
}
