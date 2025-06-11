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
    private String brand;
    private String model;
    private String year;
    private BigDecimal costPrice;
    private BigDecimal dealerPrice;
    private BigDecimal salePrice;
    private Integer stock;

    private String imageUrl;

    @NotNull
    private Long categoryProductId;
    @NotNull
    private Long supplierProductId;

    // 👉 Cantidad para la cotización
    @NotNull
    private Integer cantidad;

    // 👉 Método para calcular el subtotal
    public BigDecimal getSubtotal() {
        if (salePrice == null || cantidad == null) {
            return BigDecimal.ZERO;
        }
        return salePrice.multiply(BigDecimal.valueOf(cantidad));
    }
}
