package com.lorelis.cotizacion.service.product;

import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;

import java.util.List;

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> getAllCategory();
    CategoryDTO getCategoryById(Long id);
    void updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long id); //inhabilitar

}
