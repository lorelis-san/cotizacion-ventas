package com.lorelis.cotizacion.repository.productos;

import com.lorelis.cotizacion.model.productos.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Buscar por RUC
    Optional<Supplier> findByRuc(String ruc);

    // Verificar si existe por RUC (para validaciones)
    boolean existsByRuc(String ruc);
}
