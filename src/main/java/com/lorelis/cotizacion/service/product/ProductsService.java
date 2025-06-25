package com.lorelis.cotizacion.service.product;

import com.google.api.gax.paging.Page;
import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.model.productos.Supplier;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductsService {
    void saveProduct(ProductDTO dto, MultipartFile imageFile);
    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsEnabled();

    ProductDTO getProductById(Long id);
    void updateProduct(ProductDTO dto,MultipartFile imageFile);
    void deleteProduct(Long id);
    List<ProductDTO> buscarPorNombreOCodigo(String termino);


    List<ProductDTO> getProductsPaginated(int page, int size);

    List<ProductDTO> filterProducts(String name, String category, String brand, String year, String sort);
}
