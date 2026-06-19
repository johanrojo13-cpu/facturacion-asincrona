package com.pasarela.facturacion.service;

import com.pasarela.facturacion.model.FacturaIntegracionDto;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProcesadorFacturaService {

    private final BlockingQueue<FacturaIntegracionDto> colaFacturas = new LinkedBlockingQueue<>();
    
    private final AtomicInteger totalProcesadas = new AtomicInteger(0);

    public ProcesadorFacturaService() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    FacturaIntegracionDto factura = colaFacturas.take();
                    
                    System.out.println("\n-------------------------------------------------------------");
                    System.out.println("[2. COLA INTERNA] Un worker sacó la factura " + factura.getFacturaNumero() + " de la cola.");
                    
                    // Simulación de procesamiento pesado (3 segundos)
                    Thread.sleep(3000); 
                    
                    // [SECCIÓN 3: TRANSFORMACIÓN Y DIAN]
                    String cufe = UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
                    System.out.println("[3. DIAN UBL] Transformado a XML UBL con éxito. CUFE Generado: " + cufe);
                    System.out.println("[3. DIAN UBL] Documento enviado al Web Service de la DIAN -> Estado: APROBADO");

                    // [SECCIÓN 4: DISTRIBUCIÓN]
                    System.out.println("[4. DISTRIBUCIÓN] Generando PDF de la factura " + factura.getFacturaNumero());
                    System.out.println("[4. DISTRIBUCIÓN] Enviando correo con XML firmado y PDF adjunto...");
                    System.out.println("-------------------------------------------------------------\n");

                    // Incrementamos el contador de éxitos
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

    // Métodos para el Endpoint de Monitoreo
    public int getFacturasEnEspera() {
        return colaFacturas.size();
    }

    public int getTotalProcesadas() {
        return totalProcesadas.get();
    }
}