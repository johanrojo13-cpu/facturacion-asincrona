package com.pasarela.facturacion.model;

import java.util.List;

public class FacturaIntegracionDto {
    private String emiNit;
    private String emiNombre;
    private String adqNit;
    private String facturaNumero;
    private List<ItemDto> iteItems;

        public FacturaIntegracionDto() {}

    
    public String getEmiNit() { return emiNit; }
    public void setEmiNit(String emiNit) { this.emiNit = emiNit; }

    public String getEmiNombre() { return emiNombre; }
    public void setEmiNombre(String emiNombre) { this.emiNombre = emiNombre; }

    public String getAdqNit() { return adqNit; }
    public void setAdqNit(String adqNit) { this.adqNit = adqNit; }

    public String getFacturaNumero() { return facturaNumero; }
    public void setFacturaNumero(String facturaNumero) { this.facturaNumero = facturaNumero; }

    public List<ItemDto> getIteItems() { return iteItems; }
    public void setIteItems(List<ItemDto> iteItems) { this.iteItems = iteItems; }
}