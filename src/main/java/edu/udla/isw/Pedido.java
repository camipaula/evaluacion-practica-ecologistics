package edu.udla.isw;

public class Pedido {
    private String id_pedido;
    private String cliente;
    private String direccion;
    private String estado;

    public Pedido() {}

    public Pedido(String id_pedido, String cliente, String direccion, String estado) {
        this.id_pedido = id_pedido;
        this.cliente = cliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    public String getId_pedido() { return id_pedido; }
    public void setId_pedido(String id_pedido) { this.id_pedido = id_pedido; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}