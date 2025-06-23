package com.lorelis.cotizacion.service.vehicle;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VehicleService {

    void saveVehicle(VehicleDTO vehicleDTO);
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleById(Long id);
    void updateVehicle(VehicleDTO vehicleDTO);
    ResponseEntity<Map<String, Object>> deleteVehicle(Long id); // inhabilitar
    VehicleDTO getByPlaca(String placa);
    List<VehicleDTO> getAllVehiclesEnabled();
}
