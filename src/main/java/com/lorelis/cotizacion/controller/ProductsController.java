package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.products.SupplierDTO;
import com.lorelis.cotizacion.service.product.CategoryService;
import com.lorelis.cotizacion.service.product.ProductsService;
import com.lorelis.cotizacion.service.product.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

//    @GetMapping("/productos")
//    public String listProducts(Model model) {
//
//        List<ProductDTO> products = productsService.getAllProductsEnabled();
//
//        products.forEach(product -> {
//            if (product.getCategoryProductId() != null) {
//                CategoryDTO category = categoryService.getCategoryById(product.getCategoryProductId());
//                if (category != null) {
//                    product.setCategoryName(category.getName());
//                }
//            }
//            if (product.getSupplierProductId() != null) {
//                SupplierDTO supplier = supplierService.getSupplieryById(product.getSupplierProductId());
//                if (supplier != null) {
//                    product.setSupplierName(supplier.getName());
//                }
//            }
//        });
//
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categoryService.getAllCategory());
//        model.addAttribute("suppliers", supplierService.getAllSuppliers());
//        return "productos/productsIndex";
//    }

    @GetMapping("/productos")
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "8") int size,
                               Model model) {

        List<ProductDTO> products = productsService.getProductsPaginated(page, size);

        products.forEach(product -> {
            if (product.getCategoryProductId() != null) {
                CategoryDTO category = categoryService.getCategoryById(product.getCategoryProductId());
                if (category != null) {
                    product.setCategoryName(category.getName());
                }
            }
            if (product.getSupplierProductId() != null) {
                SupplierDTO supplier = supplierService.getSupplieryById(product.getSupplierProductId());
                if (supplier != null) {
                    product.setSupplierName(supplier.getName());
                }
            }
        });

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);

        return "productos/productsIndex";
    }

    @GetMapping("/products/filter")
    @ResponseBody
    public List<ProductDTO> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String sort
    ) {
        return productsService.filterProducts(name, category, brand, year, sort);
    }




    @GetMapping("/products/newProducto")
    public String showCreateForm(Model model) {

        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategoryEnabled());
        model.addAttribute("suppliers", supplierService.getAllSuppliersEnabled());
        return "productos/productsAgregar";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute ProductDTO product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {

        try {
                productsService.saveProduct(product, imageFile);
                redirectAttributes.addFlashAttribute("message", "Producto creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductDTO product = productsService.getProductById(id);
        if (product == null) {
            return "redirect:/productos";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategoryEnabled());
        model.addAttribute("suppliers", supplierService.getAllSuppliersEnabled());
        return "productos/productsEditar";
    }

    @PostMapping("/products/update")
    public String updateProduct(@ModelAttribute ProductDTO product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            productsService.updateProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("message", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productsService.deleteProduct(id);
            return ResponseEntity.ok("Producto eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el producto: " + e.getMessage());
        }
    }


    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        ProductDTO product = productsService.getProductById(id);
        if (product == null) {
            return "redirect:/productos";
        }

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("product", product);
        return "productos/productsView";
    }
}
