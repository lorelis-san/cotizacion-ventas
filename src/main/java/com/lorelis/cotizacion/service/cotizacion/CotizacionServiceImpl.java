package com.lorelis.cotizacion.service.cotizacion;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.dto.cotizacion.DetalleCotizacionDTO;
import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.enums.EstadoCotizacion;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import com.lorelis.cotizacion.model.cotizacion.DetalleCotizacion;
import com.lorelis.cotizacion.model.productos.Products;
import com.lorelis.cotizacion.model.vehicle.Vehicle;

import com.lorelis.cotizacion.repository.client.ClientRepository;
import com.lorelis.cotizacion.repository.cotizacion.CotizacionRepository;
import com.lorelis.cotizacion.repository.productos.ProductsRepository;
import com.lorelis.cotizacion.repository.vehicle.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CotizacionServiceImpl implements CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final ProductsRepository productsRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    public CotizacionResponseDTO mapToResponseDTO(Cotizacion cotizacion) {
        CotizacionResponseDTO dto = new CotizacionResponseDTO();
        dto.setId(cotizacion.getId());
        dto.setNumeroCotizacion(cotizacion.getNumeroCotizacion());
        dto.setEstado(cotizacion.getEstado().name());
        dto.setFecha(cotizacion.getFecha());
        dto.setObservaciones(cotizacion.getObservaciones());
        dto.setSubtotal(cotizacion.getSubtotal());
        dto.setIgv(cotizacion.getIgv());
        dto.setTotal(cotizacion.getTotal());

        // Cliente
        ClientDTO clienteDTO = new ClientDTO();
        clienteDTO.setId(cotizacion.getCliente().getId());
        clienteDTO.setTypeDocument(cotizacion.getCliente().getTypeDocument());
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
        vehiculoDTO.setPlaca(cotizacion.getVehiculo().getPlaca());
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

            detalleDTO.setProductoId(productoDTO.getId());
            detalleDTO.setCantidad(detalle.getCantidad());
            detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleDTO.setSubtotal(detalle.getSubtotal());
            return detalleDTO;
        }).toList();

        dto.setDetalles(detalles);
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
        // Generar número de cotización automático
        String numeroCotizacion = generarNumeroCotizacion();

        Cotizacion cotizacion = Cotizacion.builder()
                .numeroCotizacion(numeroCotizacion)
                .fecha(LocalDate.now())
                .cliente(cliente)
                .vehiculo(vehiculo)
                .observaciones(dto.getObservaciones())
                .estado(EstadoCotizacion.ENVIADA)
                .build();

        for (DetalleCotizacionDTO detalleDTO : dto.getDetalles()) {
            System.out.println("Producto ID recibido: " + detalleDTO.getProductoId());

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
        Cotizacion cotizacionGuardada = cotizacionRepository.save(cotizacion);
        return mapToResponseDTO(cotizacionGuardada);
    }


    private String generarNumeroCotizacion() {
        Long count = cotizacionRepository.count();
        return "COT-" + String.format("%03d", count + 1);
    }




}


//    private final DetalleCotizacionRepository detalleRepo;
//
//    @Transactional
//    @Override
//    public CotizacionDTO iniciarCotizacion() {
//        Cotizacion cot = new Cotizacion();
//        cot.setEstado("BORRADOR");
//        cot.setFechaCreacion(new Date());
//        cot = cotizacionRepo.save(cot);
//        return mapToDTO(cot);
//    }
//
//    @Transactional
//    @Override
//    public CotizacionDTO setVehiculo(Long cotizacionId, VehicleDTO vehiculoDTO) {
//        Cotizacion cot = getCotizacionOrThrow(cotizacionId);
//        Vehicle vehiculo = vehiculoRepo.findByPlaca(vehiculoDTO.getPlaca())
//                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
//        cot.setVehiculo(vehiculo);
//        return mapToDTO(cot);
//    }
//
//    @Transactional
//    @Override
//    public CotizacionDTO setCliente(Long cotizacionId, ClientDTO clienteDTO) {
//        Cotizacion cot = getCotizacionOrThrow(cotizacionId);
//        Client cliente = clienteRepo.findByDocumentNumber(clienteDTO.getDocumentNumber())
//                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
//        cot.setCliente(cliente);
//        return mapToDTO(cot);
//    }
//
//    @Transactional
//    @Override
//    public CotizacionDTO agregarDetalle(Long cotizacionId, DetalleCotizacionDTO detalleDTO) {
//        Cotizacion cot = getCotizacionOrThrow(cotizacionId);
//        Products producto = productoRepo.findById(detalleDTO.getProductoId())
//                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//
//        DetalleCotizacion detalle = new DetalleCotizacion();
//        detalle.setCotizacion(cot);
//        detalle.setProducto(producto);
//        detalle.setCantidad(detalleDTO.getCantidad());
//        detalle.setPrecioUnitario(producto.getSalePrice());
//        detalleRepo.save(detalle);
//
//        return mapToDTO(cot);
//    }
//
//    @Transactional
//    @Override
//    public CotizacionDTO eliminarDetalle(Long cotizacionId, Long productoId) {
//        Cotizacion cot = getCotizacionOrThrow(cotizacionId);
//        detalleRepo.deleteByCotizacionIdAndProductoId(cotizacionId, productoId);
//        return mapToDTO(cot);
//    }
//
//    @Override
//    public CotizacionResponseDTO iniciarCotizacion(CotizacionResponseDTO request) {
//        return null;
//    }
//
//    @Override
//    public CotizacionResponseDTO agregarProducto(Long cotizacionId, ProductDetailCotizacionDTO producto) {
//        return null;
//    }
//
//    @Override
//    public CotizacionResponseDTO eliminarProducto(Long cotizacionId, Long productoId) {
//        return null;
//    }
//
//    @Override
//    public CotizacionResponseDTO asignarVehiculo(Long cotizacionId, Long vehiculoId) {
//        return null;
//    }
//
//    @Override
//    public CotizacionResponseDTO asignarCliente(Long cotizacionId, Long clienteId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public CotizacionDTO confirmarCotizacion(Long cotizacionId) {
//        Cotizacion cot = getCotizacionOrThrow(cotizacionId);
//        List<DetalleCotizacion> detalles = detalleRepo.findByCotizacionId(cotizacionId);
//
//        BigDecimal subtotal = detalles.stream()
//                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));
//        BigDecimal total = subtotal.add(igv);
//
//        cot.setSubtotal(subtotal);
//        cot.setIgv(igv);
//        cot.setTotal(total);
//        cot.setEstado("CONFIRMADO");
//
//        return mapToDTO(cot);
//    }
//
//    @Override
//    public CotizacionResponseDTO obtenerCotizacion(Long cotizacionId) {
//        return null;
//    }
//
//    @Override
//    public List<CotizacionResponseDTO> listarCotizaciones() {
//        return List.of();
//    }
//
//    private Cotizacion getCotizacionOrThrow(Long id) {
//        return cotizacionRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
//    }
//
//    private CotizacionDTO mapToDTO(Cotizacion cot) {
//        CotizacionDTO dto = new CotizacionDTO();
//        dto.setId(cot.getId());
//        if (cot.getVehiculo() != null) {
//            VehicleDTO v = new VehicleDTO();
//            v.setId(cot.getVehiculo().getId());
//            v.setPlaca(cot.getVehiculo().getPlaca());
//            v.setMarca(cot.getVehiculo().getMarca());
//            v.setModelo(cot.getVehiculo().getModelo());
//            dto.setVehiculo(v);
//        }
//        if (cot.getCliente() != null) {
//            Client c = cot.getCliente();
//            ClientDTO cli = new ClientDTO();
//            cli.setId(c.getId());
//            cli.setFirstName(c.getFirstName());
//            cli.setLastName(c.getLastName());
//            cli.setDocumentNumber(c.getDocumentNumber());
//            cli.setEmail(c.getEmail());
//            cli.setPhoneNumber(c.getPhoneNumber());
//            dto.setCliente(cli);
//        }
//        List<DetalleCotizacion> detalles = detalleRepo.findByCotizacionId(cot.getId());
//        dto.setDetalles(detalles.stream().map(d -> {
//            DetalleCotizacionDTO det = new DetalleCotizacionDTO();
//            det.setProductoId(d.getProducto().getId());
//            det.setNombreProducto(d.getProducto().getName());
//            det.setCantidad(d.getCantidad());
//            det.setPrecioUnitario(d.getPrecioUnitario());
//            return det;
//        }).collect(Collectors.toList()));
//
//        dto.calcularTotales();
//        return dto;
//    }
