package com.lorelis.cotizacion.controller.rest;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionDTO;
import com.lorelis.cotizacion.dto.cotizacion.CotizacionResponseDTO;
import com.lorelis.cotizacion.dto.products.ProductDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.model.cotizacion.Cotizacion;
import com.lorelis.cotizacion.service.client.ClientService;
import com.lorelis.cotizacion.service.cotizacion.CotizacionService;
import com.lorelis.cotizacion.service.product.ProductsService;
import com.lorelis.cotizacion.service.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DatosCotizacionRestController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductsService productService;

    @Autowired
    private CotizacionService cotizacionService;


    @PostMapping("/cotizaciones")
    public ResponseEntity<CotizacionResponseDTO> crearCotizacion(@RequestBody CotizacionDTO dto) {
        CotizacionResponseDTO respuesta = cotizacionService.crearCotizacionDesdeDTO(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }


    @GetMapping("vehiculos/placa/{placa}")
    public ResponseEntity<VehicleDTO> buscarPorPlaca(@PathVariable String placa) {
        VehicleDTO vehiculo = vehicleService.getByPlaca(placa.trim().toUpperCase());
        if (vehiculo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(vehiculo);
    }


    @PostMapping("vehiculos/guardarVehicle")
    @ResponseBody
    public VehicleDTO guardarVehiculoDesdeApi(@RequestBody VehicleDTO vehicleDTO) {
        vehicleService.saveVehicle(vehicleDTO);
        return vehicleDTO;
    }

    //Clientes

    @GetMapping("client/documento/{numero}")
    public ClientDTO buscarPorDocumento(@PathVariable String numero) {
        ClientDTO client = clientService.getClientByDocumentNumber(numero.trim());
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return client;
    }

    @PostMapping("client/guardarClient")
    public ClientDTO guardarCliente(@RequestBody ClientDTO clientDTO) {
        clientService.saveClient(clientDTO);
        return clientDTO;
    }

    // ProductController.java
    @GetMapping("/productos/buscar")
    public List<ProductDTO> buscarProductos(@RequestParam String termino) {
        return productService.buscarPorNombreOCodigo(termino);
    }



}