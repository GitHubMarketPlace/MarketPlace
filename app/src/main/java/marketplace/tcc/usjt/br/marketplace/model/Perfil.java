package marketplace.tcc.usjt.br.marketplace.model;

import java.lang.reflect.Array;

/**
 * Created by olverajunior2014 on 20/05/17.
 */

public class Perfil {

    private String id;
    private Array products;

    public Perfil() {
    }

    public Perfil(String id, Array products) {
        this.id = id;
        this.products = products;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Array getProducts() { return products; }
    public void setProducts(Array products) { this.products = products; }
}
