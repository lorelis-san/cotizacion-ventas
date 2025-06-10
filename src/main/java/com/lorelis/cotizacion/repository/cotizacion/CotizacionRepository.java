package com.lorelis.cotizacion.repository.cotizacion;

import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
}
