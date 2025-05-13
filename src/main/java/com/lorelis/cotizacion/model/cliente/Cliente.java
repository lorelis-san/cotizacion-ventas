package com.lorelis.cotizacion.model.cliente;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombres;

    @Column(name = "apellido_cliente", nullable = false)
    private String apellidos;

    @Column(name = "nro_documento", nullable = false, unique = true)
    private String nroDocumento;  // Campo único de documento (DNI o RUC)

    @Column(name = "razon_social", nullable = true)
    private String razonSocial;  // Solo aplica si es un RUC

    @Column(name = "celular", nullable = false)
    private String celular;

    @Column(name = "correo", nullable = false)
    private String correo;


}
