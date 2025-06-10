package com.lorelis.cotizacion.repository.productos;

import com.lorelis.cotizacion.model.productos.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ProductsRepository extends JpaRepository<Products, Long> {
    boolean existsByCod(String cod);
    Optional<Products> findByCod(String cod);

    List<Products> findByNameContainingIgnoreCaseOrCodContainingIgnoreCase(String name, String cod);

}
