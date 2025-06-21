package com.lorelis.cotizacion.service.cotizacion;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.dto.cotizacion.DetalleCotizacionDTO;
import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.user.NewUserDto;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.enums.EstadoCotizacion;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import com.lorelis.cotizacion.model.cotizacion.DetalleCotizacion;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.model.usuario.User;
import com.lorelis.cotizacion.model.vehicle.Vehicle;

import com.lorelis.cotizacion.repository.client.ClientRepository;
import com.lorelis.cotizacion.repository.cotizacion.CotizacionRepository;
import com.lorelis.cotizacion.repository.cotizacion.DetalleCotizacionRepository;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import com.lorelis.cotizacion.repository.user.UserRepository;
import com.lorelis.cotizacion.repository.vehicle.VehicleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final DetalleCotizacionRepository detalleCotizacionRepository;
    private final ProductsRepository productsRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public CotizacionResponseDTO mapToResponseDTO(Cotizacion cotizacion) {
        CotizacionResponseDTO dto = new CotizacionResponseDTO();
        dto.setId(cotizacion.getId());
        dto.setNumeroCotizacion(cotizacion.getNumeroCotizacion());
        dto.setEstado(cotizacion.getEstado().name());
        dto.setFecha(cotizacion.getFecha());
        dto.setFechaCreacion(cotizacion.getFechaCreacion());
        dto.setFechaModificacion(cotizacion.getFechaModificacion());
        dto.setObservaciones(cotizacion.getObservaciones());
        dto.setSubtotal(cotizacion.getSubtotal());
        dto.setIgv(cotizacion.getIgv());
        dto.setTotal(cotizacion.getTotal());


        // Cliente
        ClientDTO clienteDTO = new ClientDTO();
        clienteDTO.setId(cotizacion.getCliente().getId());
        clienteDTO.setTypeDocument(cotizacion.getCliente().getTypeDocument().toUpperCase());
        clienteDTO.setDocumentNumber(cotizacion.getCliente().getDocumentNumber());
        clienteDTO.setFirstName(cotizacion.getCliente().getFirstName());
        clienteDTO.setLastName(cotizacion.getCliente().getLastName());
        clienteDTO.setBusinessName(cotizacion.getCliente().getBusinessName());
        clienteDTO.setEmail(cotizacion.getCliente().getEmail());
        clienteDTO.setPhoneNumber(cotizacion.getCliente().getPhoneNumber());
        dto.setCliente(clienteDTO);

        // Vehículo
        VehicleDTO vehiculoDTO = new VehicleDTO();
        vehiculoDTO.setId(cotizacion.getVehiculo().getId());
        vehiculoDTO.setPlaca(cotizacion.getVehiculo().getPlaca().toUpperCase());
        vehiculoDTO.setMarca(cotizacion.getVehiculo().getMarca());
        vehiculoDTO.setModelo(cotizacion.getVehiculo().getModelo());
        vehiculoDTO.setYear(cotizacion.getVehiculo().getYear());
        dto.setVehiculo(vehiculoDTO);

        // Detalles
        List<DetalleCotizacionDTO> detalles = cotizacion.getDetalles().stream().map(detalle -> {
            DetalleCotizacionDTO detalleDTO = new DetalleCotizacionDTO();
            ProductDTO productoDTO = new ProductDTO();
            productoDTO.setId(detalle.getProducto().getId());
            productoDTO.setName(detalle.getProducto().getName());
            productoDTO.setModel(detalle.getProducto().getModel());
            productoDTO.setBrand(detalle.getProducto().getBrand());
            productoDTO.setSalePrice(detalle.getProducto().getSalePrice());
            productoDTO.setCostPrice(detalle.getProducto().getCostPrice());
            productoDTO.setDealerPrice(detalle.getProducto().getCostDealer());

            detalleDTO.setNombreProducto(productoDTO.getName());
            detalleDTO.setProductoId(productoDTO.getId());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            return detalleDTO;
        }).toList();

        dto.setDetalles(detalles);

        if (cotizacion.getUser() != null) {
            dto.setUserNombre(cotizacion.getUser().getNombre());
            dto.setUserApellido(cotizacion.getUser().getApellido());
        }

        if (cotizacion.getUserModificador() != null) {
            dto.setUsuarioModificadorNombre(cotizacion.getUserModificador().getNombre());
            dto.setUsuarioModificadorApellido(cotizacion.getUserModificador().getApellido());
        }


        return dto;
    }

    @Override
    public Cotizacion obtenerPorId(Long id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada con ID: " + id));
    }



    @Transactional
    @Override
    public CotizacionResponseDTO crearCotizacionDesdeDTO(CotizacionDTO dto) {
        Client cliente = clientRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Vehicle vehiculo = vehicleRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        Cotizacion cotizacion = Cotizacion.builder()
                .fecha(LocalDate.now())
                .fechaCreacion(LocalDateTime.now())
                .cliente(cliente)
                .vehiculo(vehiculo)
                .user(user)
                .observaciones(dto.getObservaciones())
                .estado(EstadoCotizacion.CREADA)
                .build();

        for (DetalleCotizacionDTO detalleDTO : dto.getDetalles()) {
            Products producto = productsRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleCotizacion detalle = new DetalleCotizacion(
                    cotizacion,
                    producto,
                    detalleDTO.getCantidad(),
                    detalleDTO.getPrecioUnitario()
            );
            cotizacion.agregarDetalle(detalle);
        }

        cotizacion.calcularTotales();
        cotizacion.setNumeroCotizacion("TEMP"); // cualquier valor dummy temporal

        // ✅ 1ra vez: guardar sin número
        cotizacionRepository.save(cotizacion);

        cotizacion.setNumeroCotizacion(String.format("COT-%04d", cotizacion.getId()));
        cotizacionRepository.save(cotizacion);

        return mapToResponseDTO(cotizacion);
    }


    @Override
    public List<CotizacionResponseDTO> listarCotizaciones() {
        List<Cotizacion> cotizaciones = cotizacionRepository.findByEstadoNot(EstadoCotizacion.ELIMINADA, Sort.by(Sort.Direction.DESC, "fecha"));

        return cotizaciones.stream().map(this::mapToResponseDTO).toList();
    }


    @Override
    public ResponseEntity<Map<String, Object>> eliminarCotizacion(Long id) {
        Map<String, Object> res = new HashMap<>();
        Optional<Cotizacion> optional = cotizacionRepository.findById(id);

        if (optional.isPresent()) {
            Cotizacion cotizacion = optional.get();

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

            cotizacion.setEstado(EstadoCotizacion.ELIMINADA);
            cotizacion.setFechaModificacion(LocalDateTime.now());
            cotizacion.setUserModificador(user);

            cotizacionRepository.save(cotizacion);

            res.put("mensaje", "Cotización marcada como eliminada");
            res.put("status", HttpStatus.OK);
        } else {
            res.put("mensaje", "Cotización no encontrada con ID: " + id);
            res.put("status", HttpStatus.NOT_FOUND);
        }

        res.put("fecha", new Date());
        return ResponseEntity.status((HttpStatus) res.get("status")).body(res);
    }


    @Transactional
    @Override
    public void actualizarCotizacionDesdeDTO(CotizacionResponseDTO dto) {
        Cotizacion cotizacion = cotizacionRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada con ID: " + dto.getId()));
        // Limpiar detalles existentes
        detalleCotizacionRepository.deleteAllByCotizacionId(cotizacion.getId());

        // Agregar nuevos detalles
        List<DetalleCotizacion> nuevosDetalles = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        for (DetalleCotizacionDTO detalleDTO : dto.getDetalles()) {
            Products producto = productsRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

            DetalleCotizacion detalle = new DetalleCotizacion();
            detalle.setCotizacion(cotizacion);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setSubtotal(detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));

            nuevosDetalles.add(detalle);
        }

        detalleCotizacionRepository.saveAll(nuevosDetalles);

        BigDecimal nuevoSubtotal = nuevosDetalles.stream()
                .map(DetalleCotizacion::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal igv = nuevoSubtotal.multiply(BigDecimal.valueOf(0));
        BigDecimal total = nuevoSubtotal.add(igv);
        cotizacion.setFecha(LocalDate.now());
        cotizacion.setFechaModificacion(LocalDateTime.now());
        cotizacion.setEstado(EstadoCotizacion.MODIFICADA);
        cotizacion.setObservaciones(dto.getObservaciones());
        cotizacion.setSubtotal(nuevoSubtotal);
        cotizacion.setIgv(igv);
        cotizacion.setTotal(total);
        cotizacion.setUserModificador(user);
        cotizacionRepository.save(cotizacion);
    }

    @Override
    public CotizacionResponseDTO obtenerCotizacionResponsePorId(Long id) {
        Cotizacion cot = cotizacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada con ID: " + id));
        return mapToResponseDTO(cot);
    }


    private String generarNumeroCotizacion() {
        Long count = cotizacionRepository.count();
        return "COT-" + String.format("%03d", count + 1);
    }

}

