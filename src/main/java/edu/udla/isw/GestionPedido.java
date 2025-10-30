package edu.udla.isw;

import java.util.ArrayList;
import java.util.List;

public class GestionPedido {
    private final List<Pedido> pedidos = new ArrayList<>();

    public synchronized List<Pedido> findAll() {
        return new ArrayList<>(pedidos);
    }

    public synchronized Pedido findById(String id) {
        return pedidos.stream()
                .filter(p -> p.getId_pedido().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized void save(Pedido pedido) {
        pedidos.add(pedido);
    }

    public synchronized void saveAll(List<Pedido> lista) {
        pedidos.addAll(lista);
    }
}