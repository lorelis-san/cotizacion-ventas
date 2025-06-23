package com.lorelis.cotizacion.repository.cotizacion;

import com.lorelis.cotizacion.enums.EstadoCotizacion;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findByEstadoNot(EstadoCotizacion estado, Sort sort);
    Optional<Cotizacion> findByIdAndEstadoNot(Long id, EstadoCotizacion estado);


}
