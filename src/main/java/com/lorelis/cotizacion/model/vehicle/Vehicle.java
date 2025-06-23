package com.lorelis.cotizacion.model.vehicle;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle")
    private Long id;
    @Column(name = "placa", nullable = false, unique = true)
    private String placa;
    @Column(name = "marca", nullable = false)
    private String marca;
    @Column(name = "modelo", nullable = false)
    private String modelo;
    @Column(name = "year", nullable = false)
    private Integer year;
    private Boolean enabled = true;

}
