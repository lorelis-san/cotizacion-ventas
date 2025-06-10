package com.lorelis.cotizacion.repository.cotizacion;

import com.lorelis.cotizacion.model.cotizacion.DetalleCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCotizacionRepository extends JpaRepository<DetalleCotizacion, Long> {
    void deleteByCotizacionIdAndProductoId(Long cotizacionId, Long productoId);

    List<DetalleCotizacion> findByCotizacionId(Long cotizacionId);
}
