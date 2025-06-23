package com.lorelis.cotizacion.service.product;

import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategory();

    List<CategoryDTO> getAllCategoryEnabled();

    CategoryDTO getCategoryById(Long id);
    void updateCategory(CategoryDTO categoryDTO);
    ResponseEntity<Map<String, Object>> deleteCategory(Long id);

}
