package com.lorelis.cotizacion.model.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sede")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;
}
