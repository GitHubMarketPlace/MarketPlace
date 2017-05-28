package marketplace.tcc.usjt.br.marketplace.model;

public class Historico {

    private int order;
    private String compra;
    private String data;
    private String hora;

    public Historico() {}

    public Historico(int order, String compra, String data, String hora) {
        this.order = order;
        this.compra = compra;
        this.data = data;
        this.hora = hora;
    }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public String getCompra() { return compra; }
    public void setCompra(String compra) { this.compra = compra; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}
