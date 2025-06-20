package com.lorelis.cotizacion.service.vehicle.implement;

import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.model.client.Client;
import com.lorelis.cotizacion.model.vehicle.Vehicle;
import com.lorelis.cotizacion.repository.vehicle.VehicleRepository;
import com.lorelis.cotizacion.service.vehicle.VehicleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
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
        vehicle.setEnabled(dto.getEnabled());
        return vehicle;
    }

    private VehicleDTO convertToDTO(Vehicle vehicle){
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setPlaca(vehicle.getPlaca());
        dto.setYear(vehicle.getYear());
        dto.setMarca(vehicle.getMarca());
        dto.setModelo(vehicle.getModelo());
        dto.setEnabled(vehicle.getEnabled());
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
    public ResponseEntity<Map<String, Object>> deleteVehicle(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Vehicle> opt = vehicleRepository.findById(id);

        if (opt.isPresent()) {
            Vehicle vehicle = opt.get();
            vehicle.setEnabled(false);
            vehicleRepository.save(vehicle);
            respuesta.put("mensaje", "Vehículo deshabilitado correctamente");
            respuesta.put("status", HttpStatus.NO_CONTENT);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuesta);
        } else {
            respuesta.put("mensaje", "Vehículo no encontrado");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public VehicleDTO getByPlaca(String placa) {
        Vehicle vehicle = vehicleRepository.findByPlaca(placa);
        if (vehicle == null) {
            return null;
        }
        return convertToDTO(vehicle);
    }

    @Override
    public List<VehicleDTO> getAllVehiclesEnabled() {
        return vehicleRepository.findByEnabledTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
