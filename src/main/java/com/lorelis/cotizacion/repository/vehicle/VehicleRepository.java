package com.lorelis.cotizacion.repository.vehicle;


import com.lorelis.cotizacion.model.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByPlaca(String placa);

    List<Vehicle> findByEnabledTrue();
}
