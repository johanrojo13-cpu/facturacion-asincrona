package com.pasarela.facturacion.service;

import com.pasarela.facturacion.model.FacturaIntegracionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProcesadorFacturaService {

    private final BlockingQueue<FacturaIntegracionDto> colaFacturas = new LinkedBlockingQueue<>();
    private final AtomicInteger totalProcesadas = new AtomicInteger(0);

    // Inyectamos el puente hacia MongoDB
    @Autowired
    private MongoTemplate mongoTemplate;

    public ProcesadorFacturaService() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    FacturaIntegracionDto factura = colaFacturas.take();
                    
                    System.out.println("\n[2. COLA INTERNA] Procesando factura " + factura.getFacturaNumero());
                    Thread.sleep(3000); // Simulación
                    
                    String cufe = UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
                    System.out.println("[3. DIAN UBL] APROBADO. CUFE: " + cufe);

                    // --- ¡NUEVO: GUARDAR EN MONGODB EN TIEMPO REAL! ---
                    // Como es un DTO, Mongo lo toma, lo convierte a JSON y lo inserta en la colección "facturas"
                    mongoTemplate.save(factura, "facturas");
                    System.out.println("[💾 BASE DE DATOS] Factura " + factura.getFacturaNumero() + " guardada en MongoDB con éxito.");

                    totalProcesadas.incrementAndGet();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    public void enviarACola(FacturaIntegracionDto factura) {
        colaFacturas.add(factura);
    }

    public int getFacturasEnEspera() {
        return colaFacturas.size();
    }

    public int getTotalProcesadas() {
        return totalProcesadas.get();
    }
}