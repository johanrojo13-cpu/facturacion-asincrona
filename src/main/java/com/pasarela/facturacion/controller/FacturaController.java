package com.pasarela.facturacion.controller;

import com.pasarela.facturacion.model.FacturaIntegracionDto;
import com.pasarela.facturacion.service.ProcesadorFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private ProcesadorFacturaService procesadorService;

    @PostMapping("/recibir")
    public ResponseEntity<Map<String, String>> recibirFactura(@RequestBody FacturaIntegracionDto factura) {
        System.out.println("[1. RECEPCIÓN] Llegó la factura " + factura.getFacturaNumero() + " del cliente. Enviando a cola...");
        procesadorService.enviarACola(factura);

        return ResponseEntity.ok(Map.of(
            "status", "RECIBIDO_EN_COLA",
            "mensaje", "La factura " + factura.getFacturaNumero() + " está en fila para procesamiento asíncrono."
        ));
    }

    @GetMapping("/metricas")
    public ResponseEntity<Map<String, Object>> obtenerMetricas() {
        return ResponseEntity.ok(Map.of(
            "modulo", "Pasarela Facturación Asíncrona v1.0",
            "estadoSistema", "OPERATIVO",
            "facturasEnColaEnEsteMomento", procesadorService.getFacturasEnEspera(),
            "totalFacturasProcesadasConExito", procesadorService.getTotalProcesadas()
        ));
    }
}