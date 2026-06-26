# Pasarela de Facturación Electrónica Asíncrona (Spring Boot)

Este proyecto simula el núcleo transaccional de una pasarela de facturación electrónica en Colombia (procesamiento asíncrono estilo DIAN). Está diseñado bajo la arquitectura orientada a eventos utilizando el patrón Productor-Consumidor con colas concurrentes y seguras para entornos multi-hilo en Java.

El sistema recibe un alto volumen de facturas a través de un API REST, responde de inmediato al cliente con un estado de recibido en cola (`RECIBIDO_EN_COLA`) y procesa cada documento en segundo plano (simulando la transformación a XML UBL, firma digital, envío a la DIAN y distribución del PDF por correo).

---

## Arquitectura del Sistema

El proyecto implementa un desacoplamiento total entre el hilo que recibe las peticiones HTTP y el hilo que procesa el negocio, evitando la saturación del servidor y garantizando alta disponibilidad.

*Productor (Controller): Recibe el JSON de la factura, imprime el log de recepción y lo deposita de inmediato en la cola interna.
*Cola de Mensajería (Queue): Un componente basado en `LinkedBlockingQueue` de Java que almacena y organiza los documentos de forma segura.
*Consumidor (Worker Thread): Un hilo independiente corriendo en segundo plano que extrae las facturas de la cola una por una, aplicando un retraso simulado de 3 segundos para el proceso de validación y timbrado de la DIAN.

---

## Tecnologías Utilizadas

Java 17
Spring Boot 3.3.0 (Starter Web)
Maven (Gestor de dependencias)
Lombok (Para simplificación de código)

---

## Instalación y Ejecución Local

### Prerrequisitos
Tener instalado Java 17 o superior.
Contar con un cliente para pruebas de API (como PowerShell, cURL o Postman).

### Pasos para iniciar el servidor
1. Clona este repositorio o descarga el código fuente.
2. Abre una terminal en la raíz del proyecto.
3. Ejecuta el siguiente comando para limpiar, compilar y encender la aplicación:
   ```bash
   ./mvnw clean spring-boot:run
4. El servidor estará escuchando en el puerto 8081 una vez veas el mensaje Started FacturacionAsincronaApplication


****Endpoints Disponibles****
1. Recibir Factura (POST)
Envía una factura al sistema para ser encolada.

URL: http://localhost:8081/api/facturas/recibir
Método: POST
Content-Type: application/json
Cuerpo del Request (Ejemplo):

{
  "emiNit": "900123456",
  "emiNombre": "Proveedor Tecnologico S.A.S",
  "adqNit": "102030",
  "facturaNumero": "FE-105",
  "iteItems": [
    {
      "iteDescripcion": "Servicio Hosting Mensual",
      "itePrecio": 150000,
      "iteCantidad": 1
    }
  ]
}

***Prueba rapida desde powershell***
Invoke-RestMethod -Uri "http://localhost:8081/api/facturas/recibir" -Method Post -ContentType "application/json" -Body '{"emiNit":"900123456","emiNombre":"Proveedor Tecnologico S.A.S","adqNit":"102030","facturaNumero":"FE-110","iteItems":[{"iteDescripcion":"Servicio Hosting Mensual","itePrecio":150000,"iteCantidad":1}]}'

**Monitoreo y Métricas en Tiempo Real (GET)**
Permite observar la salud de la cola y el rendimiento de los workers en tiempo real.

URL: http://localhost:8081/api/facturas/metricas
Método: GET
Respuesta del Servidor (JSON Ejemplo):

{
  "modulo": "Pasarela Facturación Asíncrona v1.0",
  "estadoSistema": "OPERATIVO",
  "facturasEnColaEnEsteMomento": 3,
  "totalFacturasProcesadasConExito": 14
}

**Simulación de Cuello de Botella (Prueba de estrés)**
Para observar el comportamiento asíncrono y la resiliencia del sistema en tu máquina local, sigue estos pasos:

-Abre el endpoint de métricas en tu navegador: http://localhost:8081/api/facturas/metricas.
-Ejecuta el comando Invoke-RestMethod desde una terminal de PowerShell varias veces seguidas de forma rápida (presionando Flecha Arriba y Enter repetidamente).
-Refresca el navegador constantemente. Observarás cómo las facturas se acumulan temporalmente en el campo facturasEnColaEnEsteMomento y disminuyen de uno en uno de forma progresiva cada 3 segundos, demostrando que el Worker procesa la carga de manera controlada sin bloquear el flujo principal de recepción.

## Configuración del Entorno Local

El proyecto está diseñado como una pasarela de facturación electrónica asíncrona mediante mensajería y persistencia NoSQL.

### Requisitos Previos
* **Java 17 / Maven**
* **MongoDB** (Servidor corriendo localmente en el puerto `27017`)
* **Apache ActiveMQ Artemis** (Integrado en memoria de forma embebida)

### Configuración de la Base de Datos
La aplicación se conecta automáticamente a la base de datos local denominada `pasarela_dian`. 

Para inicializar el motor de la base de datos desde la consola de comandos de Windows:
```bash 
mongod