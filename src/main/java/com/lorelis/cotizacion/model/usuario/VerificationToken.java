package com.lorelis.cotizacion.model.usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    private String token;

    @OneToOne
    private Usuario usuario;

    private LocalDateTime expiryDate;
}