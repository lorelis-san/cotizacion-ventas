package com.lorelis.cotizacion.model.productos;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "El RUC/NIT es obligatorio")
    @Size(max = 11, message = "El RUC/NIT no puede exceder 11 caracteres")
    @Column(unique = true, nullable = false, length = 11)
    private String ruc; // RUC o NIT

    @Email(message = "Email debe tener formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 9, message = "El teléfono no puede exceder 9 caracteres")
    @Column(length = 9)
    private String phone;

}
