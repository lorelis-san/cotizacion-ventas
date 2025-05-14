package com.lorelis.cotizacion.service.vehicle;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;

import java.util.List;

public interface VehicleService {

    void saveVehicle(VehicleDTO vehicleDTO);
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleById(Long id);
    void updateVehicle(VehicleDTO vehicleDTO);
    void deleteVehicle(Long id); //inhabilitar

}
