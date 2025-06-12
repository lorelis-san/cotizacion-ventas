package com.lorelis.cotizacion.service.cotizacion;

import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CotizacionService {

    CotizacionResponseDTO mapToResponseDTO(Cotizacion cotizacion);
    Cotizacion obtenerPorId(Long id);
    CotizacionResponseDTO crearCotizacionDesdeDTO(CotizacionDTO dto);
    List<CotizacionResponseDTO> listarCotizaciones();
    void eliminarCotizacion(Long id);
    void actualizarCotizacionDesdeDTO(CotizacionResponseDTO dto);
    CotizacionResponseDTO obtenerCotizacionResponsePorId(Long id);



}









