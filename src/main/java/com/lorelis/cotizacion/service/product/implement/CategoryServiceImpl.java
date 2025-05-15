package com.lorelis.cotizacion.service.product.implement;

import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.model.productos.Category;
import com.lorelis.cotizacion.repository.productos.CategoryRepository;
import com.lorelis.cotizacion.service.product.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        return category;

    }

    private CategoryDTO convertToDTO(Category category){
        CategoryDTO dto= new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        return dto;
    }

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        categoryRepository.save(convertToEntity(categoryDTO));
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
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
            Category category = categoryOptional.get();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            categoryRepository.save(category);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);

    }
}
