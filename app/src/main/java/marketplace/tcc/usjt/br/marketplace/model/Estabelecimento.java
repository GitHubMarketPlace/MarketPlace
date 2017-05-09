package marketplace.tcc.usjt.br.marketplace.model;

import com.google.firebase.database.DatabaseReference;

import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.helper.Base64Helper;

public class Estabelecimento {

    private String id;
    private String numero;
    private String nome;
    private String nomeResponsavel;
    private String cnpj;
    private String email;
    private String telefone;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Estabelecimento() {
    }

    public void save(){
        DatabaseReference establishmentReference = FirebaseConfig.getFirebase();
        String id_codify = Base64Helper.codifyBase64(this.getId());
        // Dentro da tabela de users cria um nó(coluna) com o UID do usuário e passa as informações do model
        establishmentReference.child("users").child(id_codify).setValue(this);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}
