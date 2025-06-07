package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.service.product.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/suppliers")
    public String listSuppliers(Model model) {
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "suppliers/supplierIndex";
    }

    @GetMapping("/supplier/newSupplier")
    public String newSupplier(Model model) {
        model.addAttribute("supplier", new SupplierDTO());
        return "suppliers/supplierAdd";
    }
    @PostMapping("/supplier/save")
    public String saveSupplier(SupplierDTO supplierDTO) {
        supplierService.save(supplierDTO);
        return "redirect:/suppliers";
    }

    @GetMapping("/supplier/edit/{id}")
    public String editSupplier(@PathVariable Long id, Model model) {
        SupplierDTO supplier = supplierService.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        model.addAttribute("supplier", supplier);
        return "suppliers/supplierEdit";
    }

    @PostMapping("/supplier/update/{id}")
    public String updateSupplier(@PathVariable Long id, @ModelAttribute SupplierDTO supplierDTO) {
        supplierService.update(id, supplierDTO);
        return "redirect:/suppliers";
    }

    @GetMapping("/supplier/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.deleteById(id);
        return "redirect:/suppliers";
    }

}
