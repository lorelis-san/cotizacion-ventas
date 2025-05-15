package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.service.product.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String redirigirACategorias() {
        return "redirect:/categories";
    }
    @GetMapping("/categories")
    public String listarCategorias(Model model) {
        List<CategoryDTO> listaCategorias = categoryService.getAllCategory();
        model.addAttribute("listaCategorias", listaCategorias);
        return "categories/categoriesIndex"; // Asegúrate de tener esta vista
    }

    @GetMapping("/nuevaCategoria")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new CategoryDTO());
        return "categories/categoriesAgregar"; // Asegúrate de tener esta vista
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") CategoryDTO categoryDTO) {
        categoryService.saveCategory(categoryDTO);
        return "redirect:/categories";
    }


    @PostMapping("/actualizarCategoria")
    public String actualizarCategoria(@ModelAttribute("categoria") CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return "redirect:/categories";
    }

    @GetMapping("/categoria/{id}")
    @ResponseBody
    public CategoryDTO obtenerCategoriaPorId(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }


    @GetMapping("/eliminarCategoria/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

















}
