package com.lorelis.cotizacion.model.cotizacion;

import com.lorelis.cotizacion.model.cliente.Cliente;
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
@Table(name = "cotizacion")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    private Long id;

    @Column(name = "nro_cotizacion", nullable = false, unique = true)
    private String nroCotizacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "estado", nullable = false)
    private String estado;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleCotizacion> detallesCotizacion;


    // Relación con el contador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contador", nullable = false)
    private ContadorCotizacion contadorCotizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();

        // Formatear el número de cotización con ceros a la izquierda
        this.nroCotizacion = generarNroCotizacion();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Método para generar el número de cotización
    private String generarNroCotizacion() {
        Long contador = contadorCotizacion.incrementarContador();
        return String.format("N° %09d", contador);
    }
}
