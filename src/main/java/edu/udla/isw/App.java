package edu.udla.isw;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Aplicación EcoLogistics - versión con entidad Pedido.
 * Lee archivos CSV desde /input y expone endpoints REST.
 */
public class App extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        Main camel = new Main();
        camel.bind("gestionPedido", new GestionPedido());
        camel.configure().addRoutesBuilder(new App());
        camel.run(args);
    }

    @Override
    public void configure() {

        // === Configuración REST ===
        restConfiguration()
            .component("netty-http")
            .port(7760)
            .contextPath("/api")
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "API de Pedidos - EcoLogistics")
            .apiProperty("api.version", "1.0.0");

        // === Rutas REST ===
        rest("/pedidos").description("Gestión de pedidos")
            .get()
                .produces("application/json")
                .to("direct:listarPedidos")
            .post()
                .consumes("application/json")
                .produces("application/json")
                .to("direct:crearPedido");

        rest("/pedidos/{id_pedido}")
            .get()
                .produces("application/json")
                .to("direct:buscarPedido");

        // === Implementación de endpoints ===
        from("direct:listarPedidos")
            .bean("gestionPedido", "findAll")
            .marshal().json()
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"));

        from("direct:buscarPedido")
            .process(exchange -> {
                String id = exchange.getMessage().getHeader("id_pedido", String.class);
                Pedido pedido = ((GestionPedido) getContext().getRegistry()
                        .lookupByName("gestionPedido")).findById(id);

                if (pedido == null) {
                    exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
                    exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    exchange.getMessage().setBody("{\"error\":\"Pedido no encontrado\"}");
                } else {
                    exchange.getMessage().setBody(pedido);
                }
            })
            .marshal().json()
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"));

        from("direct:crearPedido")
            .unmarshal().json(Pedido.class)
            .bean("gestionPedido", "save")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setBody(simple("{\"mensaje\":\"Pedido creado exitosamente\"}"));

        // === Ruta: leer archivo CSV desde carpeta input ===
        from("file:input?include=.*\\.csv&noop=true")
            .routeId("lectura-pedidos")
            .log("Archivo CSV detectado: ${file:name}")
            .unmarshal().csv()
            .process(exchange -> {
                @SuppressWarnings("unchecked")
                List<List<String>> filas = exchange.getMessage().getBody(List.class);
                List<Pedido> registros = new ArrayList<>();
                boolean cabecera = true;

                for (List<?> fila : filas) {
                    if (cabecera) { cabecera = false; continue; }
                    String id = leer(fila, 0);
                    String cliente = leer(fila, 1);
                    String direccion = leer(fila, 2);
                    String estado = leer(fila, 3);
                    registros.add(new Pedido(id, cliente, direccion, estado));
                }

                exchange.getMessage().setBody(registros);
            })
            .bean("gestionPedido", "saveAll")
            .log("Cargados ${body.size()} pedidos en memoria desde CSV");
    }

    private static String leer(List<?> fila, int idx) {
        if (fila == null || idx >= fila.size() || fila.get(idx) == null) return "";
        return String.valueOf(fila.get(idx)).trim();
    }
}