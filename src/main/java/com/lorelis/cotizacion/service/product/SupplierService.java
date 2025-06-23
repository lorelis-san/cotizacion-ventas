package com.lorelis.cotizacion.service.product;

import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.model.productos.Supplier;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SupplierService {

    List<SupplierDTO> getAllSuppliers();
    Optional<SupplierDTO> findById(Long id);
    SupplierDTO getSupplieryById(Long id);
    SupplierDTO save(SupplierDTO supplierDTO);
    SupplierDTO update(Long id, SupplierDTO supplierDTO);
    ResponseEntity<Map<String, Object>>  deleteById(Long id);
    List<SupplierDTO> getAllSuppliersEnabled();
    // Métodos de conversión
    SupplierDTO convertToDTO(Supplier supplier);
    Supplier convertToEntity(SupplierDTO supplierDTO);
}
