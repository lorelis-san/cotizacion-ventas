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
        String placa = vehicleDTO.getPlaca().trim().toUpperCase();
        if (vehicleRepository.findByPlaca(placa) != null) {
            throw new RuntimeException("Ya existe un vehículo con esa placa");
        }
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
        if (vehicleDTO.getId() == null) return;
        Optional<Vehicle> existingVehicleOpt = vehicleRepository.findById(vehicleDTO.getId());
        if (existingVehicleOpt.isPresent()) {
            Vehicle existingVehicle = existingVehicleOpt.get();

            // Validar que la nueva placa no exista en otro vehículo
            Vehicle vehicleConMismaPlaca = vehicleRepository.findByPlaca(vehicleDTO.getPlaca().trim().toUpperCase());
            if (vehicleConMismaPlaca != null && !vehicleConMismaPlaca.getId().equals(vehicleDTO.getId())) {
                throw new RuntimeException("Ya existe otro vehículo con esa placa");
            }

            // Actualiza los campos del vehículo
            existingVehicle.setPlaca(vehicleDTO.getPlaca().trim().toUpperCase());
            existingVehicle.setMarca(vehicleDTO.getMarca());
            existingVehicle.setModelo(vehicleDTO.getModelo());
            existingVehicle.setYear(vehicleDTO.getYear());
            vehicleRepository.save(existingVehicle);
        }
    }


    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleDTO getByPlaca(String placa) {
        Vehicle vehicle = vehicleRepository.findByPlaca(placa);
        if (vehicle == null) {
            return null;
        }
        return convertToDTO(vehicle);
    }
}
