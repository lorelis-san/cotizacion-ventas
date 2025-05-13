package com.lorelis.cotizacion.dto.quote;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class QuoteDetailDTO {
    private Long id;

    @NotNull
    private Long idQuote; // Referencia a la cotización (solo ID)

    @NotNull
    private Long idProduct; // Referencia al producto (solo ID)

    @NotNull
    private Integer amount;

    private BigDecimal salePrice;

    private BigDecimal totalPrice;
}
