package com.lorelis.cotizacion.service.usuario.implement;

import com.lorelis.cotizacion.model.usuario.Sede;
import com.lorelis.cotizacion.service.usuario.SedeService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SedeServiceImpl implements SedeService {

    @Override
    public List<Sede> listarSedes() {
        List<Sede> sedes = new ArrayList<>();
        sedes.add(new Sede(1L, "Trujillo 1", null));
        sedes.add(new Sede(2L, "Trujillo 2", null));
        return sedes;
    }
}