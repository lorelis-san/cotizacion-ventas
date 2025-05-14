package com.lorelis.cotizacion.controller;

import com.lorelis.cotizacion.dto.client.ClientDTO;
import com.lorelis.cotizacion.dto.vehicle.VehicleDTO;
import com.lorelis.cotizacion.service.vehicle.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String guardarVehicle(@ModelAttribute("vehicle") VehicleDTO vehicle) {

        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicle";

    }

    @GetMapping("/nuevoVehiculo")
    public String mostrarFormularioNuevoVehiculo(Model model) {
        model.addAttribute("vehicle", new VehicleDTO());
        return "vehicle/vehicleAgregar";
    }

    // Actualizar cliente
    @PostMapping("/actualizarVehiculo")
    public String actualizarVehicle(@ModelAttribute VehicleDTO vehicleDTO) {
        vehicleService.updateVehicle(vehicleDTO);
        return "redirect:/vehicle";
    }

    // Obtener cliente por ID para editar (API endpoint para AJAX)
    @GetMapping("/vehicle/{id}")
    @ResponseBody
    public VehicleDTO obtenerVehiculoPorId(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }


    @GetMapping("/eliminarVehicle/{id}")
    public String eliminarVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicle";
    }



}
