package marketplace.tcc.usjt.br.marketplace.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;

public class Historico {

    private String id;
    private String order;
    private String compra;
    private String data;
    private String hora;

    public Historico() {}

    public Historico(String id, String order, String compra, String data, String hora) {
        this.id = id;
        this.order = order;
        this.compra = compra;
        this.data = data;
        this.hora = hora;
    }

    public void save(String sequence){
        DatabaseReference historicReference = FirebaseConfig.getFirebase();
        // Dentro da tabela de users cria um nó(coluna) com o UID do usuário e passa as informações do model
        historicReference.child("historics").child(getId()).child(sequence).setValue(this);
    }

    @Exclude
    public String getId() { return id;}
    public void setId(String id) { this.id = id; }

    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }

    public String getCompra() { return compra; }
    public void setCompra(String compra) { this.compra = compra; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}
