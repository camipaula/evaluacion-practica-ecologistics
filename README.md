===========================================
   SISTEMA DE INTEGRACIÓN DE PEDIDOS
          ECOLOGISTICS - UDLA
===========================================

1. PROPÓSITO DEL PROYECTO
--------------------------
Este sistema fue diseñado como una prueba de integración basada en Apache Camel.
Su objetivo es procesar automáticamente pedidos almacenados en formato CSV y
ponerlos a disposición mediante un servicio REST accesible en localhost.

El flujo automatiza tareas que antes eran manuales, eliminando errores de
transcripción y mejorando la rapidez con la que los datos pueden ser consultados
por otros sistemas.

-------------------------------------------

2. FLUJO DE FUNCIONAMIENTO
--------------------------
- Apache Camel monitorea la carpeta `input` en busca de archivos `.csv`.
- Cuando detecta un archivo válido, lo lee, convierte las filas a objetos `Pedido`
  y los mantiene en memoria a través de la clase `GestionPedido`.
- Finalmente, los datos quedan disponibles para ser consumidos mediante la API REST.

-------------------------------------------

3. ENDPOINTS DISPONIBLES
------------------------
Los servicios REST se ejecutan por defecto en el puerto **7760**.

> URL base:  http://localhost:7760/api

• **GET /pedidos**
  - Devuelve todos los pedidos cargados desde el CSV o creados vía POST.

• **GET /pedidos/{id_pedido}**
  - Permite buscar un pedido específico por su identificador.

• **POST /pedidos**
  - Agrega un nuevo pedido al sistema.
  - Cuerpo esperado (JSON):
    {
      "id_pedido": "004",
      "cliente": "María López",
      "direccion": "Av. Amazonas y Colón",
      "estado": "Pendiente"
    }

-------------------------------------------

4. EJECUCIÓN DEL SISTEMA
------------------------
1. Asegúrate de tener Java 17 y Maven instalados.
2. Desde la raíz del proyecto, ejecuta:
      mvn exec:java
3. Camel iniciará y mostrará en consola el procesamiento del archivo CSV.
4. Puedes probar los endpoints desde Postman o tu navegador web.

*(Alternativamente, puedes importar el proyecto en IntelliJ o VS Code
y ejecutar directamente la clase `App.java`.)*

-------------------------------------------

5. RECURSOS TÉCNICOS
--------------------
- Lenguaje: Java 17
- Framework de integración: Apache Camel 4.x
- Dependencias gestionadas con Maven
- Servidor embebido Netty HTTP
- Formato de datos: CSV → JSON (en memoria)


-------------------------------------------
