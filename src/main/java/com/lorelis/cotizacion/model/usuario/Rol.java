package com.lorelis.cotizacion.model.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // Ej. ADMIN, VENDEDOR
}
