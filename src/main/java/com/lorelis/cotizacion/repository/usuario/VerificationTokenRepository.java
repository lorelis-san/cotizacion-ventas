package com.lorelis.cotizacion.repository.usuario;


import com.lorelis.cotizacion.model.usuario.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);
}