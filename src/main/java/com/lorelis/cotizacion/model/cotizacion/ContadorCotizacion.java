package com.lorelis.cotizacion.model.cotizacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class ContadorCotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "contador", nullable = false)
    private Long contador;

    public Long incrementarContador() {
        this.contador++;
        return this.contador;
    }

}

