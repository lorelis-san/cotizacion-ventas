package com.lorelis.cotizacion.service.cotizacion;

import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CotizacionService {

    CotizacionResponseDTO mapToResponseDTO(Cotizacion cotizacion);
    Cotizacion obtenerPorId(Long id);
    CotizacionResponseDTO crearCotizacionDesdeDTO(CotizacionDTO dto);
    List<CotizacionResponseDTO> listarCotizaciones();
    ResponseEntity<Map<String, Object>> eliminarCotizacion(Long id);
    void actualizarCotizacionDesdeDTO(CotizacionResponseDTO dto);
    CotizacionResponseDTO obtenerCotizacionResponsePorId(Long id);



}









