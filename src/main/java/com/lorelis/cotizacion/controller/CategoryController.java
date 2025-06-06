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
    public String redirigirADashboard() {
        return "fragments/dashboard";
    }
    @GetMapping("/categories")
    public String listarCategorias(Model model) {
        List<CategoryDTO> listaCategorias = categoryService.getAllCategory();
        model.addAttribute("listaCategorias", listaCategorias);
        return "categories/categoriesIndex";
    }

    @GetMapping("/nuevaCategoria")
    public String mostrarFormularioNuevaCategoria(Model model) {
        model.addAttribute("categoria", new CategoryDTO());
        return "categories/categoriesAgregar";
    }

    @PostMapping("/categories/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") CategoryDTO categoryDTO, Model model) {
        try {
            categoryService.saveCategory(categoryDTO);
            return "redirect:/categories";
        } catch (RuntimeException e) {
            model.addAttribute("categoria", categoryDTO);
            model.addAttribute("error", e.getMessage());
            return "categories/categoriesAgregar";
        }
    }

    @GetMapping("/categoria/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable Long id, Model model) {
        CategoryDTO categoria = categoryService.getCategoryById(id);
        model.addAttribute("categoria", categoria);
        return "categories/categoriesEditar";
    }


    @PostMapping("/actualizarCategoria")
    public String actualizarCategoria(@ModelAttribute("categoria") CategoryDTO categoryDTO, Model model) {
        try{categoryService.updateCategory(categoryDTO);
        return "redirect:/categories";
        } catch (RuntimeException e) {
            model.addAttribute("categoria", categoryDTO);
            model.addAttribute("error", e.getMessage());
            return "categories/categoriesEditar";
        }
    }




    @GetMapping("/eliminarCategoria/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

















}
