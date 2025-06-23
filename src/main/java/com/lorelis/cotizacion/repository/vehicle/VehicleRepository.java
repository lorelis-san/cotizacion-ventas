package com.lorelis.cotizacion.repository.vehicle;


import com.lorelis.cotizacion.model.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByPlaca(String placa);
    Vehicle findByPlacaAndEnabledTrue(String placa);

    List<Vehicle> findByEnabledTrue();
}
