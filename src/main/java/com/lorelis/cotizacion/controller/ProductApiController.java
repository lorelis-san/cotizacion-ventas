package com.lorelis.cotizacion.controller;


import com.lorelis.cotizacion.dto.products.ProductFilterDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.service.product.ProductServiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductApiController {

    @Autowired
    private ProductServiceList productService;

    @GetMapping
    public Page<ProductListDTO> listarProductos(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.listarProductos(pageable);
    }

//    @GetMapping("/buscar")
//    public Page<ProductListDTO> buscarProductos(
//            @ModelAttribute ProductFilterDTO filter,
//            Pageable pageable
//    ) {
//        return productService.buscarProductosDTO(filter, pageable);
//    }

}