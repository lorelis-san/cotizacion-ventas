package com.lorelis.cotizacion.service.product;

import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.model.productos.Supplier;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductsService {
    void saveProduct(ProductDTO dto, MultipartFile imageFile);
    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsEnabled();

    ProductDTO getProductById(Long id);
    void updateProduct(ProductDTO dto,MultipartFile imageFile);
    void deleteProduct(Long id);
    List<ProductDTO> buscarPorNombreOCodigo(String termino);

    Page<ProductListDTO> listarProductos(Pageable pageable);

    List<ProductListDTO> filtrarProductos(String categoria, String marca, Integer anio);

    List<ProductListDTO> buscarListaPorNombreOCodigo(String termino);

    Map<String, List<String>> obtenerOpcionesFiltros();
}