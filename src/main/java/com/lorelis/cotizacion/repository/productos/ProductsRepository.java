package com.lorelis.cotizacion.repository.productos;

import com.lorelis.cotizacion.model.productos.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> , JpaSpecificationExecutor<Products> {
    boolean existsByCod(String cod);
    List<Products> findByNameContainingIgnoreCaseOrCodContainingIgnoreCase(String name, String cod);
    List<Products> findByEnabledTrue();
    Optional<Products> findByIdAndEnabledTrue(Long id);
    boolean existsByCodAndEnabledTrue(String cod);

@Query("SELECT DISTINCT p FROM Products p WHERE p.enabled = true AND " +
        "(LOWER(p.name) LIKE LOWER(CONCAT('%', :termino, '%')) " +
        "OR LOWER(p.cod) LIKE LOWER(CONCAT('%', :termino, '%')))")
List<Products> buscarActivosPorNombreOCodigo(@Param("termino") String termino);

    Page<Products> findByEnabledTrue(Pageable pageable);
    Products findByCod(String cod);

    // NUEVAS QUERIES PARA FILTROS

    @Query("SELECT DISTINCT p FROM Products p WHERE p.enabled = true " +
            "AND (:categoria IS NULL OR LOWER(p.categoryproduct.name) = LOWER(:categoria)) " +
            "AND (:marca IS NULL OR LOWER(p.brand) = LOWER(:marca)) " +
            "AND (:año IS NULL OR p.startYear <= :año AND (p.endYear IS NULL OR p.endYear >= :año))")
    List<Products> filtrarProductosActivos(@Param("categoria") String categoria,
                                           @Param("marca") String marca,
                                           @Param("año") Integer año);

    @Query("SELECT DISTINCT p.categoryproduct.name FROM Products p WHERE p.enabled = true AND p.categoryproduct.name IS NOT NULL ORDER BY p.categoryproduct.name")
    List<String> obtenerCategoriasUnicas();

    @Query("SELECT DISTINCT p.brand FROM Products p WHERE p.enabled = true AND p.brand IS NOT NULL ORDER BY p.brand")
    List<String> obtenerMarcasUnicas();

    @Query("SELECT DISTINCT p.startYear FROM Products p WHERE p.enabled = true AND p.startYear IS NOT NULL " +
            "UNION " +
            "SELECT DISTINCT p.endYear FROM Products p WHERE p.enabled = true AND p.endYear IS NOT NULL " +
            "ORDER BY 1")
    List<Integer> obtenerAniosUnicos();



//    @Query("SELECT p FROM Products p WHERE p.brand = :marca AND p.model = :modelo AND p.year = :year AND p.enabled = true")
//    List<Products> findByMarcaModeloYear(@Param("marca") String marca, @Param("modelo") String modelo, @Param("year") Integer year);


    @Query("SELECT p FROM Products p " +
            "WHERE p.enabled = true " +
            "AND p.brand = :brand " +
            "AND p.model = :model " +
            "AND :year BETWEEN p.startYear AND p.endYear")
    List<Products> findByMarcaModeloYearInRange(
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("year") Integer year);


}
