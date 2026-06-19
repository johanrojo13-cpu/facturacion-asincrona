package com.pasarela.facturacion.model;

import java.math.BigDecimal;

public class ItemDto {
    private String iteDescripcion;
    private BigDecimal itePrecio;
    private int iteCantidad;

    public ItemDto() {}

    public String getIteDescripcion() { return iteDescripcion; }
    public void setIteDescripcion(String iteDescripcion) { this.iteDescripcion = iteDescripcion; }

    public BigDecimal getItePrecio() { return itePrecio; }
    public void setItePrecio(BigDecimal itePrecio) { this.itePrecio = itePrecio; }

    public int getIteCantidad() { return iteCantidad; }
    public void setIteCantidad(int iteCantidad) { this.iteCantidad = iteCantidad; }
}