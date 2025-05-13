package com.lorelis.cotizacion.model.cotizacion;
import jakarta.persistence.*;
@Entity
public class RespuestaCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    private Cotizacion cotizacion;

    private String respuesta; // Ej. "Aceptada", "Rechazada"
    private String comentario;

    // Getters y setters
}
