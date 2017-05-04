package marketplace.tcc.usjt.br.marketplace.model;

/**
 * Created by olverajunior2014 on 20/03/17.
 */

public class Categoria {

    private int id;
    private String nome ;
    private Object produto;

    public Categoria() {}

    public Categoria(int id, String nome, Object produto) {
        this.id = id;
        this.nome = nome;
        this.produto = produto;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public Object getProduct() {
        return produto;
    }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setProduct(Object produto) {
        this.produto = produto;
    }

}
