package com.lorelis.cotizacion.enums;

public enum EstadoCotizacion {
    BORRADOR("Borrador"),
    ENVIADA("Enviada"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
    VENCIDA("Vencida");

    private final String descripcion;

    EstadoCotizacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}