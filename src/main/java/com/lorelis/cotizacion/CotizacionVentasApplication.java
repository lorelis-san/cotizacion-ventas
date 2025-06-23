package com.lorelis.cotizacion;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class CotizacionVentasApplication {

	@PostConstruct
	public void init() {
		// Establecer zona horaria por defecto
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) {
		SpringApplication.run(CotizacionVentasApplication.class, args);
	}

}
