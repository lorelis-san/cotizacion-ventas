package com.lorelis.cotizacion.dto.products;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListDTO {
    private Long id;
    private String cod;
    private String name;
    private String brand;
    private String model;
    private String imageUrl;
    private BigDecimal salePrice;
    private BigDecimal costDealer;
    private String category;      // Nuevo campo
    private Integer startYear;    // Nuevo campo
    private Integer endYear;      // Nuevo campo

    public ProductListDTO(Long id, String cod, String name, String brand, String model, String imageUrl,
                          BigDecimal salePrice, BigDecimal costDealer, String category, Integer yearStart, Integer yearEnd) {
        this.id = id;
        this.cod = cod;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.imageUrl = imageUrl;
        this.salePrice = salePrice;
        this.costDealer = costDealer;
        this.category = category;
        this.startYear = yearStart;
        this.endYear = yearEnd;
    }
}
