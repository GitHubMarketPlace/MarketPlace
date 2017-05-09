package marketplace.tcc.usjt.br.marketplace.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Usuario;

public class CadastroEstabelecimentoActivity extends AppCompatActivity {

    // Atributos do Firebase
    private FirebaseAuth firebaseAuth;
    // Atributos da Activity
    private ProgressBar spinner;
    private EditText cadastro_nomeEmpresa;
    private EditText cadastro_nomeResponsavel;
    private EditText cadastro_emailEmp;
    private EditText cadastro_senhaEmp;
    private EditText cadastro_cnpj;
    private EditText cadastro_telEmp;
    private EditText cadastro_ruaEmp;
    private EditText cadastro_numeroEmp;
    private EditText cadastro_bairroEmp;
    private EditText cadastro_cidadeEmp;
    private EditText cadastro_ufEmp;
    private EditText cadastro_cepEmp;
    private AlertDialog.Builder dialog_complete;
    private AlertDialog.Builder dialog_error;
    private String error_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estabelecimento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instanciando variáveis do firebase
        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        // Reconhece os elementos da view
        cadastro_nomeEmpresa     = (EditText)findViewById(R.id.cadastro_nomeEmpresa);
        cadastro_nomeResponsavel = (EditText)findViewById(R.id.cadastro_nomeResponsavel);
        cadastro_emailEmp        = (EditText)findViewById(R.id.cadastro_emailEmp);
        cadastro_senhaEmp        = (EditText)findViewById(R.id.cadastro_senhaEmp);
        cadastro_cnpj            = (EditText)findViewById(R.id.cadastro_cnpj);
        cadastro_telEmp          = (EditText)findViewById(R.id.cadastro_telEmp);
        cadastro_ruaEmp          = (EditText)findViewById(R.id.cadastro_ruaEmp);
        cadastro_numeroEmp       = (EditText)findViewById(R.id.cadastro_numeroEmp);
        cadastro_bairroEmp       = (EditText)findViewById(R.id.cadastro_bairroEmp);
        cadastro_cidadeEmp       = (EditText)findViewById(R.id.cadastro_cidadeEmp);
        cadastro_ufEmp           = (EditText)findViewById(R.id.cadastro_ufEmp);
        cadastro_cepEmp          = (EditText)findViewById(R.id.cadastro_cepEmp);
        spinner                  = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);

        // Definição das mascaras dos inputs
//        SimpleMaskFormatter simpleMaskCnpj = new SimpleMaskFormatter("NNN.NNN.NNN.NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskEstado = new SimpleMaskFormatter("LL");
        SimpleMaskFormatter simpleMaskCep = new SimpleMaskFormatter("NNNNN-NNN");
        //Coloca um lintener nos inputs que precisam de máscara
//        MaskTextWatcher maskCpf = new MaskTextWatcher(cadastro_cpf, simpleMaskCpf);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(cadastro_telEmp, simpleMaskTelefone);
        MaskTextWatcher maskEstado = new MaskTextWatcher(cadastro_ufEmp, simpleMaskEstado);
        MaskTextWatcher maskCep = new MaskTextWatcher(cadastro_cepEmp, simpleMaskCep);
        // Adiciona a máscara
//        cadastro_cpf.addTextChangedListener(maskCpf);
        cadastro_telEmp.addTextChangedListener(maskTelefone);
        cadastro_ufEmp.addTextChangedListener(maskEstado);
        cadastro_cepEmp.addTextChangedListener(maskCep);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return super.onCreateOptionsMenu(menu); }

    // Método de cadastro de usuário
    public void registerEstablishment(View view) {
        spinner.setVisibility(View.VISIBLE);
        // Cria uma relação do usuário no Firebase //
        firebaseAuth.createUserWithEmailAndPassword(cadastro_emailEmp.getText().toString(),cadastro_senhaEmp.getText().toString())
                .addOnCompleteListener(CadastroEstabelecimentoActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            createAndSaveUserObject(task);
                            spinner.setVisibility(View.GONE);
                            createSuccessDialog();
                        } else {
                            try {
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                error_message = "Digite uma senha que contenha letras números e no mínimo 6 caractéres.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                error_message = "E-mail digitado inválido, digite um e-mail válido.";
                            } catch (FirebaseAuthInvalidUserException e) {
                                error_message = "Usuário inválido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                error_message = "Este e-mail já está em uso.";
                            } catch (Exception e) {
                                error_message = "Digite uma senha que contenha letras números e no mínimo 6 caractéres.";
                                e.printStackTrace();
                            }

                            createErrorDialog(error_message);
                            spinner.setVisibility(View.GONE);
                        }
                    }});
    }

    public void createAndSaveUserObject(Task<AuthResult> task){
        // Cria o objeto do usuário e salva os dados no banco
        Usuario user = new Usuario();
        FirebaseUser userFirebase = task.getResult().getUser();
        user.setId(userFirebase.getUid());
        user.setNome(cadastro_nomeEmpresa.getText().toString());
        user.setSobrenome(cadastro_nomeResponsavel.getText().toString());
        user.setEmail(cadastro_emailEmp.getText().toString());
        user.setSenha(cadastro_senhaEmp.getText().toString());
        user.setCpf(cadastro_cnpj.getText().toString());
        user.setTelefone(cadastro_telEmp.getText().toString());
        user.setRua(cadastro_ruaEmp.getText().toString());
        user.setNumero(cadastro_numeroEmp.getText().toString());
        user.setBairro(cadastro_bairroEmp.getText().toString());
        user.setCidade(cadastro_cidadeEmp.getText().toString());
        user.setEstado(cadastro_ufEmp.getText().toString());
        user.setCep(cadastro_cepEmp.getText().toString());
        user.save();
        if (user != null){
            DatabaseReference establishmentReference = FirebaseConfig.getFirebase();
            establishmentReference.child("establishments").child(user.getId()).setValue(user);
        }
    }

    public void createErrorDialog(String error_message){
        dialog_error = new AlertDialog.Builder(CadastroEstabelecimentoActivity.this);
        dialog_error.setTitle("Erro!");
        dialog_error.setCancelable(false);
        dialog_error.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        dialog_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_error.setMessage(error_message);
        dialog_error.create();
        dialog_error.show();
    }

    public void createSuccessDialog(){
        //Criando Dialog de confirmação
        dialog_complete = new AlertDialog.Builder(CadastroEstabelecimentoActivity.this);
        dialog_complete.setTitle("Sucesso!");
        dialog_complete.setMessage("O perfil do estabelecimento foi cadastrado com sucesso");
        dialog_complete.setCancelable(false);
        dialog_complete.setIcon(android.R.drawable.stat_sys_upload_done);
        dialog_complete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearValues();
                finish();
            }
        });
        dialog_complete.create();
        dialog_complete.show();
    }

    public void clearValues(){
        cadastro_nomeEmpresa.setText("");
        cadastro_nomeResponsavel.setText("");
        cadastro_emailEmp.setText("");
        cadastro_senhaEmp.setText("");
        cadastro_cnpj.setText("");
        cadastro_telEmp.setText("");
        cadastro_ruaEmp.setText("");
        cadastro_numeroEmp.setText("");
        cadastro_bairroEmp.setText("");
        cadastro_cidadeEmp.setText("");
        cadastro_ufEmp.setText("");
        cadastro_cepEmp.setText("");
    }

}
