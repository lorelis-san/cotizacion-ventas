package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.products.CategoryDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.service.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicle")
    public String listarVehiculos(Model model) {
        List<VehicleDTO> listVehiculo = vehicleService.getAllVehicles();

        // Si no hay datos, pasar una lista vacía
        if (listVehiculo == null) {
            listVehiculo = new ArrayList<>();
        }
        model.addAttribute("listVehiculo", listVehiculo);
        return "vehicle/vehicleIndex";
    }

    @PostMapping("/vehicle/guardar")
    public String guardarVehicle(@ModelAttribute("vehicle") VehicleDTO vehicle, Model model) {
        try {
            vehicleService.saveVehicle(vehicle);
            return "redirect:/vehicle";
        } catch (RuntimeException e) {
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("error", e.getMessage());
            return "vehicle/vehicleAgregar";
        }
    }

    @GetMapping("/nuevoVehiculo")
    public String mostrarFormularioNuevoVehiculo(Model model) {
        model.addAttribute("vehicle", new VehicleDTO());
        return "vehicle/vehicleAgregar";
    }

    @GetMapping("/vehicle/existsByPlaca")
    @ResponseBody
    public boolean existsPlaca(@RequestParam String placa, @RequestParam(required = false) Long id) {
        VehicleDTO existingVehicle = vehicleService.getByPlaca(placa.trim());

        return existingVehicle != null && (id == null || !existingVehicle.getId().equals(id));
    }

    @GetMapping("/vehicle/{id}")
    public String mostrarFormularioEditarVehiculo(@PathVariable Long id, Model model) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicle);
        return "vehicle/vehicleEditar";
    }

    // Actualizar vehículo
    @PostMapping("/actualizarVehiculo")
    public String actualizarVehicle(@ModelAttribute VehicleDTO vehicleDTO, Model model) {
        try {
            vehicleService.updateVehicle(vehicleDTO);
            return "redirect:/vehicle";
        } catch (RuntimeException e) {
            model.addAttribute("vehicle", vehicleDTO);
            model.addAttribute("error", e.getMessage());
            return "vehicle/vehicleEditar";
        }
    }

    @GetMapping("/eliminarVehicle/{id}")
    public String eliminarVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicle";
    }




}
