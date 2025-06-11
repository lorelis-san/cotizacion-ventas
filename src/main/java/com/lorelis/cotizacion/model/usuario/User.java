package com.lorelis.cotizacion.model.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

//    @NotBlank
//    @Column(unique = true, nullable = false)
//    private String nombre;
//    @NotBlank
//    @Column(unique = true, nullable = false)
//    private String apellido;
//
    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

//    @NotBlank
//    @Column(unique = true, nullable = false)
//    private String email;
//
    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
