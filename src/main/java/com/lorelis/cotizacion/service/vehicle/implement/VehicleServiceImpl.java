package com.lorelis.cotizacion.service.vehicle.implement;

import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.model.vehicle.Vehicle;
import com.lorelis.cotizacion.repository.vehicle.VehicleRepository;
import com.lorelis.cotizacion.service.vehicle.VehicleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle convertToEntity(VehicleDTO dto){
        Vehicle vehicle = new Vehicle();
        vehicle.setId((dto.getId()));
        vehicle.setPlaca(dto.getPlaca());
        vehicle.setYear(dto.getYear());
        vehicle.setMarca(dto.getMarca());
        vehicle.setModelo(dto.getModelo());

        return vehicle;
    }

    private VehicleDTO convertToDTO(Vehicle vehicle){
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setPlaca(vehicle.getPlaca());
        dto.setYear(vehicle.getYear());
        dto.setMarca(vehicle.getMarca());
        dto.setModelo(vehicle.getModelo());

        return dto;
    }

    @Override
    public void saveVehicle(VehicleDTO vehicleDTO) {

        vehicleRepository.save(convertToEntity(vehicleDTO));
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(id);
        return vehicleOpt.map(this::convertToDTO).orElse(null);
    }

    @Override
    public void updateVehicle(VehicleDTO vehicleDTO) {
      if (vehicleDTO.getId()== null) return;
      Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleDTO.getId());

      if(vehicleOptional.isPresent()){
          Vehicle vehicle = vehicleOptional.get();
          vehicle.setPlaca(vehicleDTO.getPlaca());
          vehicle.setYear(vehicleDTO.getYear());
          vehicle.setMarca(vehicleDTO.getMarca());
          vehicle.setModelo(vehicleDTO.getModelo());

          vehicleRepository.save(vehicle);

      }

    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
