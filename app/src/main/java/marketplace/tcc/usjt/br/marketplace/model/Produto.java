package marketplace.tcc.usjt.br.marketplace.model;

import com.google.firebase.database.DatabaseReference;

import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;

public class Produto {

    private String id;
    private String nome;
    private String descricao;
    private String valor;
    private String valor_promocao;
    private String nome_proprietario;
    private String categoria;

    public Produto() {
    }

    public void save(){
        DatabaseReference userReference = FirebaseConfig.getFirebase();
        // Dentro da tabela de products cria um nó(coluna) com o ID do produto e passa as informações do model
        userReference.child("products").child(getId()).setValue(this);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome_proprietario() { return nome_proprietario; }
    public void setNome_proprietario(String nome_proprietario) { this.nome_proprietario = nome_proprietario; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getValor_promocao() { return valor_promocao; }
    public void setValor_promocao(String valor_promocao) { this.valor_promocao = valor_promocao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}

