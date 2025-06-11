package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.model.productos.Supplier;
import com.lorelis.cotizacion.repository.productos.SupplierRepository;
import com.lorelis.cotizacion.service.product.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        if (supplierRepository.existsByRuc(supplierDTO.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con ese RUC");
        }
        Supplier supplier = convertToEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
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
    public void deleteById(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
        supplierRepository.deleteById(id);
    }

    @Override
    public SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setRuc(supplier.getRuc());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
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
        return supplier;
    }
}
