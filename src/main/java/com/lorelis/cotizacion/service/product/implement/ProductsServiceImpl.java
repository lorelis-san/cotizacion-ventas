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
        product.setYear(dto.getYear());
        product.setCostPrice(dto.getCostPrice());
        product.setCostDealer(dto.getDealerPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setImageUrl(dto.getImageUrl());

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
        dto.setYear(product.getYear());
        dto.setCostPrice(product.getCostPrice());
        dto.setDealerPrice(product.getCostDealer());
        dto.setSalePrice(product.getSalePrice());
        dto.setImageUrl(product.getImageUrl());

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

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(imageFile);
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar la imagen: " + e.getMessage());
            }
        }
        productsRepository.save(product);
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

    @Override
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
            product.setYear(dto.getYear());
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
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    // Solo eliminar la imagen anterior si se va a subir una nueva
                    String oldImageUrl = product.getImageUrl();
                    // Subir la nueva imagen
                    String newImageUrl = firebaseStorageService.uploadFile(imageFile);
                    product.setImageUrl(newImageUrl);
                    // Eliminar la imagen anterior después de subir la nueva exitosamente
                    if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                        try {
                            firebaseStorageService.deleteFile(oldImageUrl);
                            System.out.println("Imagen anterior eliminada: " + oldImageUrl);
                        } catch (Exception e) {
                            System.err.println("Advertencia: No se pudo eliminar la imagen anterior: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error al actualizar la imagen: " + e.getMessage());
                }
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
            // Eliminar la imagen si existe
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                firebaseStorageService.deleteFile(product.getImageUrl());
            }
            productsRepository.deleteById(id);
        }
    }

    // validación

    private void validarProductoDTO(ProductDTO dto) {
        validarCod(dto);
        validarYear(dto);
        validarPrice(dto);
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
        if (dto.getYear() != null && !dto.getYear().trim().isEmpty()) {
            try {
                int anio = Integer.parseInt(dto.getYear());
                int anioActual = java.time.LocalDate.now().getYear();
                if (anio < 1900 || anio > anioActual) {
                    throw new IllegalArgumentException("El año debe estar entre 1900 y " + anioActual);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El año debe ser un número válido.");
            }
        }
    }

    private void validarPrice(ProductDTO dto) {
        if (dto.getCostPrice() != null && dto.getSalePrice() != null) {
            if (dto.getSalePrice().compareTo(dto.getCostPrice()) < 0) {
                throw new IllegalArgumentException("El precio de venta no puede ser menor que el costo.");
            }
        }
    }
}
