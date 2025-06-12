package com.lorelis.cotizacion.dto.quote;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuoteDTO {
    private Long id;

    @NotNull
    private String nroQuote;

    @NotNull
    private Long clientId; // Referencia al cliente (solo ID)

    @NotNull
    private Long vehicleId;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private String coment;

    private BigDecimal total;

    @NotNull
    private String state; // El estado puede ser un String o Enum dependiendo de cómo lo quieras manejar

    private List<QuoteDetailDTO> quoteDetails;

    @NotNull
    private Long counterQuoteId;
}
