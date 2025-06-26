package com.lorelis.cotizacion.service.product;

import com.lorelis.cotizacion.dto.products.ProductFilterDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;


@Service
public class ProductServiceList {

    @Autowired
    private ProductsRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProductListDTO> listarProductos(Pageable pageable) {
        Page<Products> productos = productRepository.findByEnabledTrue(pageable);

        return productos.map(p -> new ProductListDTO(
                p.getId(),
                p.getCod(),
                p.getName(),
                p.getBrand(),
                p.getModel(),
                p.getImageUrl(),
                p.getSalePrice(),
                p.getCostDealer(),
                p.getCategoryproduct().getName(),     // agregado
                p.getStartYear(),    // agregado
                p.getEndYear()       // agregado
        ));
    }




}
