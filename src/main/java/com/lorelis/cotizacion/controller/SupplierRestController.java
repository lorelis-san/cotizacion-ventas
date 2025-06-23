package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.service.product.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/api/suppliers")
public class SupplierRestController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<SupplierDTO>> getAllSuppliersApi() {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<SupplierDTO> getSupplierByIdApi(@PathVariable Long id) {
        Optional<SupplierDTO> supplier = supplierService.findById(id);
        return supplier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<SupplierDTO> createSupplierApi(@Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO savedSupplier = supplierService.save(supplierDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<SupplierDTO> updateSupplierApi(@PathVariable Long id,
                                                         @Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO updatedSupplier = supplierService.update(id, supplierDTO);
            return ResponseEntity.ok(updatedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSupplierApi(@PathVariable Long id) {
        try {
            supplierService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
