package com.lorelis.cotizacion.service.cotizacion;

import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import jakarta.transaction.Transactional;

public interface CotizacionService {

//    @Transactional
//    CotizacionDTO iniciarCotizacion();
//
//    @Transactional
//    CotizacionDTO setVehiculo(Long cotizacionId, VehicleDTO vehiculoDTO);
//
//    @Transactional
//    CotizacionDTO setCliente(Long cotizacionId, ClientDTO clienteDTO);
//
//    @Transactional
//    CotizacionDTO agregarDetalle(Long cotizacionId, DetalleCotizacionDTO detalleDTO);
//
//    @Transactional
//    CotizacionDTO eliminarDetalle(Long cotizacionId, Long productoId);
//
//    CotizacionResponseDTO iniciarCotizacion(CotizacionResponseDTO request);
//
//    CotizacionResponseDTO agregarProducto(Long cotizacionId, ProductDetailCotizacionDTO producto);
//
//    CotizacionResponseDTO eliminarProducto(Long cotizacionId, Long productoId);
//
//    CotizacionResponseDTO asignarVehiculo(Long cotizacionId, Long vehiculoId);
//
//    CotizacionResponseDTO asignarCliente(Long cotizacionId, Long clienteId);
//
//    CotizacionResponseDTO confirmarCotizacion(Long cotizacionId);
//
//    CotizacionResponseDTO obtenerCotizacion(Long cotizacionId);
//
//    List<CotizacionResponseDTO> listarCotizaciones();

    CotizacionResponseDTO crearCotizacionDesdeDTO(CotizacionDTO dto);
}









