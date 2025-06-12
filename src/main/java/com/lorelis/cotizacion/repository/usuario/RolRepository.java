package com.lorelis.cotizacion.repository.usuario;
import com.lorelis.cotizacion.model.usuario.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
