package com.lorelis.cotizacion.controller;


import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
import com.lorelis.cotizacion.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductApiController {

    @Autowired
    private ProductsService productService;

    @GetMapping
    public Page<ProductListDTO> listarProductos(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "9") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.listarProductos(pageable);
    }


    @GetMapping("/buscarProducto/{termino}")
    public List<ProductDTO> buscarProductos(@PathVariable String termino) {
        return productService.buscarPorNombreOCodigo(termino);
    }

}