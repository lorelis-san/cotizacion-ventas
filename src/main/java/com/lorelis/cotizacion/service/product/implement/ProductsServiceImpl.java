package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.model.productos.Category;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.model.productos.Supplier;
import com.lorelis.cotizacion.repository.productos.CategoryRepository;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import com.lorelis.cotizacion.repository.productos.SupplierRepository;
import com.lorelis.cotizacion.service.firebaseStorage.FirebaseStorageService;
import com.lorelis.cotizacion.service.product.ProductsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    private Products convertToEntity(ProductDTO dto) {
        Products product = new Products();
        product.setId(dto.getId());
        product.setCod(dto.getCod());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBrand(dto.getBrand());
        product.setModel(dto.getModel());
        product.setStartYear(dto.getStartYear());
        product.setEndYear(dto.getEndYear());
        product.setCostPrice(dto.getCostPrice());
        product.setCostDealer(dto.getDealerPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setImageUrl(dto.getImageUrl());
        product.setSede(dto.getSede());

        if (dto.getCategoryProductId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(dto.getCategoryProductId());
            categoryOptional.ifPresent(product::setCategoryproduct);
        }
        if (dto.getSupplierProductId() != null && dto.getSupplierProductId() > 0) {
            Optional<Supplier> supplierOptional = supplierRepository.findById(dto.getSupplierProductId());
            if (supplierOptional.isPresent()) {
                product.setSupplierProduct(supplierOptional.get());
            } else {
                throw new jakarta.persistence.EntityNotFoundException("Proveedor no encontrado con ID: " + dto.getSupplierProductId());
            }
        }
        return product;
    }

    private ProductDTO convertToDTO(Products product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCod(product.getCod());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setModel(product.getModel());
        dto.setStartYear(product.getStartYear());
        dto.setEndYear(product.getEndYear());
        dto.setCostPrice(product.getCostPrice());
        dto.setDealerPrice(product.getCostDealer());
        dto.setSalePrice(product.getSalePrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setSede(product.getSede());

        if (product.getCategoryproduct() != null) {
            dto.setCategoryProductId(product.getCategoryproduct().getId());
        }
        if (product.getSupplierProduct() != null) {
            dto.setSupplierProductId(product.getSupplierProduct().getId());
        }
        return dto;
    }

    @Override
    public void saveProduct(ProductDTO dto, MultipartFile imageFile) {
        validarProductoDTO(dto);
        Products product = convertToEntity(dto);

        System.out.println("Guardando producto con URL de imagen: " + dto.getImageUrl());

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = firebaseStorageService.uploadFile(imageFile);
                product.setImageUrl(imageUrl);
                System.out.println("Imagen subida desde archivo: " + imageUrl);
            }
            else if (dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty()) {
                try {
                    String imageUrl = firebaseStorageService.uploadFileFromUrl(dto.getImageUrl());
                    product.setImageUrl(imageUrl);
                    System.out.println("Imagen subida desde URL: " + imageUrl);
                } catch (IOException e) {
                    System.err.println("Error al procesar imagen desde URL: " + e.getMessage());
                    // Continuar guardando el producto sin imagen en lugar de fallar completamente
                    product.setImageUrl(null);
                    System.out.println("Producto se guardará sin imagen debido al error");
                }
            }
            System.out.println("DTO START YEAR: " + dto.getStartYear());
            System.out.println("DTO END YEAR: " + dto.getEndYear());
            productsRepository.save(product);
            System.out.println("Producto guardado exitosamente con ID: " + product.getId());

        } catch (IOException e) {
            System.err.println("Error al procesar imagen desde archivo: " + e.getMessage());
            throw new RuntimeException("Error al cargar la imagen desde archivo: " + e.getMessage());
        }
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Optional<Products> productOptional = productsRepository.findById(id);
        return productOptional.map(this::convertToDTO).orElse(null);
    }


    public void updateProduct(ProductDTO dto, MultipartFile imageFile) {
        validarProductoDTO(dto);

        if (dto.getId() == null) return;

        Optional<Products> productOptional = productsRepository.findById(dto.getId());
        if (productOptional.isPresent()) {
            Products product = productOptional.get();

            product.setCod(dto.getCod());
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setBrand(dto.getBrand());
            product.setModel(dto.getModel());
            product.setStartYear(dto.getStartYear());
            product.setEndYear(dto.getEndYear());
            product.setCostPrice(dto.getCostPrice());
            product.setCostDealer(dto.getDealerPrice());
            product.setSalePrice(dto.getSalePrice());

            if (dto.getCategoryProductId() != null) {
                Optional<Category> categoryOptional = categoryRepository.findById(dto.getCategoryProductId());
                categoryOptional.ifPresent(product::setCategoryproduct);
            }
            if (dto.getSupplierProductId() != null && dto.getSupplierProductId() > 0) {
                Optional<Supplier> supplierOptional = supplierRepository.findById(dto.getSupplierProductId());
                if (supplierOptional.isPresent()) {
                    product.setSupplierProduct(supplierOptional.get());
                } else {
                    throw new jakarta.persistence.EntityNotFoundException("Proveedor no encontrado con ID: " + dto.getSupplierProductId());
                }
            }
            String oldImageUrl = product.getImageUrl();
            boolean imageUpdated = false;
            try {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String newImageUrl = firebaseStorageService.uploadFile(imageFile);
                    product.setImageUrl(newImageUrl);
                    imageUpdated = true;
                    System.out.println("Imagen actualizada desde archivo: " + newImageUrl);
                }
                else if (dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty() &&
                        !dto.getImageUrl().equals(oldImageUrl)) {
                    try {
                        String newImageUrl = firebaseStorageService.uploadFileFromUrl(dto.getImageUrl());
                        product.setImageUrl(newImageUrl);
                        imageUpdated = true;
                        System.out.println("Imagen actualizada desde URL: " + newImageUrl);
                    } catch (IOException e) {
                        System.err.println("Error al procesar nueva imagen desde URL: " + e.getMessage());
                    }
                }

                if (imageUpdated && oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    try {
                        firebaseStorageService.deleteFile(oldImageUrl);
                        System.out.println("Imagen anterior eliminada: " + oldImageUrl);
                    } catch (Exception e) {
                        System.err.println("Advertencia: No se pudo eliminar la imagen anterior: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al procesar imagen desde archivo: " + e.getMessage());
                throw new RuntimeException("Error al actualizar la imagen desde archivo: " + e.getMessage());
            }
            productsRepository.save(product);
            System.out.println("Producto actualizado exitosamente - ID: " + product.getId());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Products> productOptional = productsRepository.findById(id);

        if (productOptional.isPresent()) {
            Products product = productOptional.get();
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty() &&
                    product.getImageUrl().contains("firebasestorage.googleapis.com")) {
                firebaseStorageService.deleteFile(product.getImageUrl());
            }
            productsRepository.deleteById(id);
        }
    }

    private void validarProductoDTO(ProductDTO dto) {
        validarCod(dto);
        validarYear(dto);
        validarPrice(dto);
        validarSede(dto);
    }

    private void validarCod(ProductDTO dto) {
        if (dto.getCod() == null || dto.getCod().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }
        if (dto.getId() == null && productsRepository.existsByCod(dto.getCod())) {
            throw new IllegalArgumentException("El código ya esta registrado");
        }
    }

    private void validarYear(ProductDTO dto) {
        Integer start = dto.getStartYear();
        Integer end = dto.getEndYear();
        int anioActual = java.time.LocalDate.now().getYear();

        if (start == null || start < 1900 || start > anioActual + 10) {
            throw new IllegalArgumentException("El año inicial debe estar entre 1900 y " + (anioActual + 10));
        }

        if (end != null && (end < start || end > anioActual + 20)) {
            throw new IllegalArgumentException("El año final debe ser mayor o igual al inicial y razonable");
        }
    }

    private void validarPrice(ProductDTO dto) {
        if (dto.getCostPrice() != null && dto.getSalePrice() != null) {
            if (dto.getSalePrice().compareTo(dto.getCostPrice()) < 0) {
                throw new IllegalArgumentException("El precio de venta no puede ser menor que el costo.");
            }
        }
    }

    private void validarSede(ProductDTO dto) {
        if (dto.getSede() == null || dto.getSede().trim().isEmpty()) {
            throw new IllegalArgumentException("La sede es obligatoria");
        }
        List<String> sedesValidas = Arrays.asList("Lima", "Chiclayo", "Trujillo", "Piura", "Arequipa");
        if (!sedesValidas.contains(dto.getSede())) {
            throw new IllegalArgumentException("La sede seleccionada no es válida");
        }
    }
}
