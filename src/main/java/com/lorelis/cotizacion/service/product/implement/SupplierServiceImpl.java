package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.model.productos.Supplier;
import com.lorelis.cotizacion.repository.productos.SupplierRepository;
import com.lorelis.cotizacion.service.product.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SupplierDTO> findById(Long id) {
        return supplierRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public SupplierDTO getSupplieryById(Long id) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        return supplierOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public SupplierDTO save(SupplierDTO supplierDTO) {
        String ruc = supplierDTO.getRuc();
        Optional<Supplier> existing = supplierRepository.findByRuc(ruc);

        if (existing.isPresent()) {
            Supplier existingSupplier = existing.get();

            Boolean isEnabled = existingSupplier.getEnabled();
            if (Boolean.TRUE.equals(isEnabled)) {
                throw new RuntimeException("Ya existe un proveedor con ese RUC.");
            }

            // Reactivar proveedor deshabilitado
            existingSupplier.setName(supplierDTO.getName());
            existingSupplier.setEmail(supplierDTO.getEmail());
            existingSupplier.setPhone(supplierDTO.getPhone());
            existingSupplier.setEnabled(true);
            Supplier updatedSupplier = supplierRepository.save(existingSupplier);
            return convertToDTO(updatedSupplier);
        }

        // Crear nuevo proveedor
        Supplier newSupplier = convertToEntity(supplierDTO);
        newSupplier.setEnabled(true);
        Supplier savedSupplier = supplierRepository.save(newSupplier);
        return convertToDTO(savedSupplier);
    }



    @Override
    public SupplierDTO update(Long id, SupplierDTO supplierDTO) {


        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    // Validar RUC solo si cambió
                    if (!existingSupplier.getRuc().equals(supplierDTO.getRuc()) &&
                            existsByRuc(supplierDTO.getRuc())) {
                        throw new RuntimeException("Ya existe un proveedor con ese RUC: " + supplierDTO.getRuc());
                    }

                    // Actualizar campos
                    existingSupplier.setName(supplierDTO.getName());
                    existingSupplier.setRuc(supplierDTO.getRuc());
                    existingSupplier.setEmail(supplierDTO.getEmail());
                    existingSupplier.setPhone(supplierDTO.getPhone());

                    Supplier updatedSupplier = supplierRepository.save(existingSupplier);
                    return convertToDTO(updatedSupplier);
                })
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    public boolean existsByRuc(String ruc) {
        return supplierRepository.existsByRuc(ruc);
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteById(Long id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);


        if (optionalSupplier.isEmpty()) {
            res.put("mensaje", "Proveedor no encontrado");
            res.put("status", HttpStatus.NOT_FOUND);
        } else {
            Supplier supplier = optionalSupplier.get();
            supplier.setEnabled(false);  // Solo lo deshabilita
            supplierRepository.save(supplier);  // Guarda el cambio

            res.put("mensaje", "Proveedor deshabilitado correctamente");
            res.put("status", HttpStatus.OK);
        }
        res.put("fecha", new Date());
        return ResponseEntity.status((HttpStatus) res.get("status")).body(res);
    }

    @Override
    public List<SupplierDTO> getAllSuppliersEnabled() {
        return supplierRepository.findByEnabledTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setRuc(supplier.getRuc());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setEnabled(supplier.getEnabled());
        return dto;
    }

    @Override
    public Supplier convertToEntity(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setId(supplierDTO.getId());
        supplier.setName(supplierDTO.getName());
        supplier.setRuc(supplierDTO.getRuc());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setEnabled(supplierDTO.getEnabled());
        return supplier;
    }
}
