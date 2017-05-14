package marketplace.tcc.usjt.br.marketplace.model;

public class Categoria {

    private String id;
    private String nome ;
    private Object produto;

    public Categoria() {}

    public Categoria(String id, String nome, Object produto) {
        this.id = id;
        this.nome = nome;
        this.produto = produto;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public Object getProduct() {
        return produto;
    }

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setProduct(Object produto) {
        this.produto = produto;
    }

}
