package com.lorelis.cotizacion.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SedeDTO {
    private Long id;

    @NotNull
    private String name;

    private String address;
}
