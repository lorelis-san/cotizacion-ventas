package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.ProductListDTO;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        product.setYear(dto.getYear());
        product.setStartYear(dto.getStartYear());
        product.setEndYear(dto.getEndYear());
        product.setCostPrice(dto.getCostPrice());
        product.setCostDealer(dto.getDealerPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setImageUrl(dto.getImageUrl());
        product.setSede(dto.getSede());
        product.setEnabled(dto.getEnabled());
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
        dto.setStartYear(product.getStartYear());
        dto.setEndYear(product.getEndYear());
        dto.setCostPrice(product.getCostPrice());
        dto.setDealerPrice(product.getCostDealer());
        dto.setSalePrice(product.getSalePrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setSede(product.getSede());
        dto.setEnabled(product.getEnabled());
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
        validarProductoDTO(dto); // Asegúrate de no lanzar error si ya existe pero está deshabilitado

        String cod = dto.getCod().trim();
        Products existente = productsRepository.findByCod(cod);

        if (existente != null) {
            if (!existente.getEnabled()) {
                // Reactivar y actualizar datos
                dto.setId(existente.getId()); // importante para que JPA lo actualice
                updateProduct(dto, imageFile); // usa el método de actualización
                return;
            } else {
                throw new IllegalArgumentException("El código ya está registrado en un producto activo.");
            }
        }

        // Nuevo producto
        Products product = convertToEntity(dto);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = firebaseStorageService.uploadFile(imageFile);
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al cargar la imagen desde archivo: " + e.getMessage());
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
    public List<ProductDTO> getAllProductsEnabled() {
        return productsRepository.findByEnabledTrue().stream()
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
            product.setYear(dto.getYear());
            product.setStartYear(dto.getStartYear());
            product.setEndYear(dto.getEndYear());
            product.setCostPrice(dto.getCostPrice());
            product.setCostDealer(dto.getDealerPrice());
            product.setSalePrice(dto.getSalePrice());
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
            // Manejar imagen
            System.out.println("va a entrar a la img URL: ");
            String oldImageUrl = product.getImageUrl();
            boolean imageUpdated = false;
            try {
                System.out.println("entra al if");
                // Prioridad 1: Nuevo archivo subido
                if (imageFile != null && !imageFile.isEmpty()) {
                    System.out.println("a la linea 183: ");
                    String newImageUrl = firebaseStorageService.uploadFile(imageFile);
                    product.setImageUrl(newImageUrl);
                    imageUpdated = true;
                    System.out.println("Imagen actualizada desde archivo: " + newImageUrl);
                }
                // Prioridad 2: Nueva URL proporcionada (diferente a la actual)
                else if (dto.getImageUrl() != null && !dto.getImageUrl().trim().isEmpty() &&
                        !dto.getImageUrl().equals(oldImageUrl)) {

                    try {
                        String newImageUrl = firebaseStorageService.uploadFileFromUrl(dto.getImageUrl());
                        product.setImageUrl(newImageUrl);
                        imageUpdated = true;
                        System.out.println("Imagen actualizada desde URL: " + newImageUrl);
                    } catch (IOException e) {
                        System.err.println("Error al procesar nueva imagen desde URL: " + e.getMessage());
                        // No actualizar la imagen, mantener la anterior
                        System.out.println("Se mantendrá la imagen anterior debido al error");
                    }
                }

                // Eliminar imagen anterior solo si se actualizó exitosamente
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

            // Guardar producto actualizado
            productsRepository.save(product);
            System.out.println("Producto actualizado exitosamente - ID: " + product.getId());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Products> productOptional = productsRepository.findById(id);

        if (productOptional.isPresent()) {
            Products product = productOptional.get();

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()
                    && product.getImageUrl().contains("firebasestorage.googleapis.com")) {
                try {
                    firebaseStorageService.deleteFile(product.getImageUrl());
                    System.out.println("Imagen eliminada de Firebase: " + product.getImageUrl());
                } catch (Exception e) {
                    System.err.println("Advertencia: No se pudo eliminar la imagen: " + e.getMessage());
                }
                product.setImageUrl(null);
            }


            product.setEnabled(false);
            productsRepository.save(product);
            System.out.println("Producto deshabilitado con ID: " + product.getId());
        }
    }


    @Override
    public List<ProductDTO> buscarPorNombreOCodigo(String termino) {
        List<Products> productos = productsRepository.buscarActivosPorNombreOCodigo(termino);
        return productos.stream().map(this::convertToDTO).collect(Collectors.toList());
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

        Products existente = productsRepository.findByCod(dto.getCod().trim());
        if (existente != null) {
            // Si es nuevo (sin ID) y ya existe habilitado, error
            if (dto.getId() == null && existente.getEnabled()) {
                throw new IllegalArgumentException("El código ya está registrado en un producto activo");
            }
            // Si está deshabilitado, permitir continuar y actualizar
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

    @Override
    public Page<ProductListDTO> listarProductos(Pageable pageable) {
        Page<Products> productos = productsRepository.findByEnabledTrue(pageable);

        return productos.map(p -> new ProductListDTO(
                p.getId(),
                p.getCod(),
                p.getName(),
                p.getBrand(),
                p.getModel(),
                p.getImageUrl(),
                p.getSalePrice(),
                p.getCostDealer(),
                p.getCategoryproduct().getName(),
                p.getStartYear(),
                p.getEndYear()
        ));
    }
}
