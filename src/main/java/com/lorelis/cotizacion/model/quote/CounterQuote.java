package com.lorelis.cotizacion.model.quote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class CounterQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "counter", nullable = false)
    private Long counter;

    public Long incrementCounter() {
        this.counter++;
        return this.counter;
    }

}

