package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.model.productos.Category;
import com.lorelis.cotizacion.repository.productos.CategoryRepository;
import com.lorelis.cotizacion.service.product.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category convertToEntity(CategoryDTO dto){
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setEnabled(dto.getEnabled());
        return category;

    }

    private CategoryDTO convertToDTO(Category category){
        CategoryDTO dto= new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setEnabled(category.getEnabled());
        return dto;
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        String nombre = categoryDTO.getName().trim().toLowerCase();
        Category existing = categoryRepository.findByName(nombre);

        if (existing != null) {
            if(existing.getEnabled()){
                throw new RuntimeException("Ya existe una categoría con ese nombre");
            }
            else{
                existing.setName(categoryDTO.getName());
                existing.setDescription(categoryDTO.getDescription());
                existing.setEnabled(true);
                categoryRepository.save(existing);
                return;
            }

        }
        Category nuevaCategoria= convertToEntity(categoryDTO);
        nuevaCategoria.setEnabled(true);
        categoryRepository.save(nuevaCategoria);

    }


    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override
    public List<CategoryDTO> getAllCategoryEnabled() {
        return categoryRepository.findByEnabledTrue().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getId()== null) return;
        Optional<Category> categoryOptional = categoryRepository.findById(categoryDTO.getId());

        if(categoryOptional.isPresent()){
            Category existingCategory = categoryRepository.findByName(categoryDTO.getName());
            if (existingCategory != null && !existingCategory.getId().equals(categoryDTO.getId())){
                throw new RuntimeException("Ya existe una categoría con ese nombre");
            }

            Category category = categoryOptional.get();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            categoryRepository.save(category);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteCategory(Long id) {
        Map<String, Object> res = new HashMap<>();
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            res.put("mensaje", "Categoría no encontrada");
            res.put("status", HttpStatus.NOT_FOUND);
            res.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        Category category = categoryOptional.get();
        category.setEnabled(false);
        categoryRepository.save(category);

        res.put("mensaje", "Categoría deshabilitada correctamente");
        res.put("status", HttpStatus.NO_CONTENT);
        res.put("fecha", new Date());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }
}
