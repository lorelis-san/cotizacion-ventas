package com.lorelis.cotizacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class CotizacionVentasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotizacionVentasApplication.class, args);

	}
//	@PostConstruct
//	public void init(){
//		// Fuerza zona horaria a Lima (UTC-5)
//		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
//		System.out.println("Zona horaria establecida: " + TimeZone.getDefault());
//
//
//	}


}
