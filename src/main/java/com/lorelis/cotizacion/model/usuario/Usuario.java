package com.lorelis.cotizacion.model.usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password; // Usar encriptación para la contraseña

    // Relación con la entidad Rol
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;
}
