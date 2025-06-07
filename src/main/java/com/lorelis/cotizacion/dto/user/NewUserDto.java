package com.lorelis.cotizacion.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    public String username;
    public String password;

}
