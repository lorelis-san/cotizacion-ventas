package com.lorelis.cotizacion.model.quote;
import jakarta.persistence.*;
@Entity
public class RespuestaCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    private Quote cotizacion;

    private String respuesta; // Ej. "Aceptada", "Rechazada"
    private String comentario;

    // Getters y setters
}
