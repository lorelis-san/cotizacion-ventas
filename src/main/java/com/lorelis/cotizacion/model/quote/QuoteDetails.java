package com.lorelis.cotizacion.model.quote;
import com.lorelis.cotizacion.model.productos.Productos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "QuoteDetails")
public class QuoteDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id", nullable = false)
    private Quote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Productos product;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "salePrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "totalPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;



    public void calcularPrecioTotal() {
        if (amount != null && salePrice != null) {
            this.totalPrice = salePrice.multiply(BigDecimal.valueOf(amount));
        }
    }
}

