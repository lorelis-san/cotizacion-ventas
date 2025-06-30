package com.lorelis.cotizacion.controller;


import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductApiController {

    @Autowired
    private ProductsService productService;

    @GetMapping
    public Page<ProductListDTO> listarProductos(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.listarProductos(pageable);
    }

    @GetMapping("/buscarProducto")
    public List<ProductListDTO> buscarProductos(@RequestParam String termino) {
        return productService.buscarListaPorNombreOCodigo(termino);
    }

    // NUEVOS ENDPOINTS PARA FILTROS
    @GetMapping("/filtrar")
    public List<ProductListDTO> filtrarProductos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer anio) {

        return productService.filtrarProductos(categoria, marca, anio);
    }

    @GetMapping("/filtros/opciones")
    public Map<String, List<String>> obtenerOpcionesFiltros() {
        return productService.obtenerOpcionesFiltros();
    }



    @GetMapping("/porVehiculo")
    public ResponseEntity<List<ProductDTO>> obtenerProductosPorVehiculo(
            @RequestParam String marca,
            @RequestParam String modelo,
            @RequestParam Integer year) {
        List<Products> productos = productService.obtenerProductosPorMarcaModeloYear(marca, modelo, year);
        List<ProductDTO> productosDTO = productos.stream()
                .map(productService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productosDTO);
    }



}