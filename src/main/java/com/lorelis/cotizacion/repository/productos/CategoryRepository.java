package com.lorelis.cotizacion.repository.productos;

import com.lorelis.cotizacion.model.productos.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
