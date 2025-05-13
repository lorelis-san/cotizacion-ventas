package com.lorelis.cotizacion.dto.products;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    @NotNull
    private String name;

    private String cod;

    private String description;

    private String brand; //marca

    private String model;

    private String year;

    private BigDecimal costPrice;

    private BigDecimal dealerPrice;

    private BigDecimal salePrice;

    private Integer stock;

    private String imageUrl;

    @NotNull
    private Long categoryProductId; // Referencia a la Categoria
}
