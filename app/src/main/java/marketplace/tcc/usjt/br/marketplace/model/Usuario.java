package marketplace.tcc.usjt.br.marketplace.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;

public class Usuario {

    private String id;
    private String numero;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Usuario() {
    }

    public void save(){
        DatabaseReference userReference = FirebaseConfig.getFirebase();
        // Dentro da tabela de users cria um nó(coluna) com o UID do usuário e passa as informações do model
        userReference.child("users").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() { return id;}
    public void setId(String id) { this.id = id; }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) { this.numero = numero; }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
}
