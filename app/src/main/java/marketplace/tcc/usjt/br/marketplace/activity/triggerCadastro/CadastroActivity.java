package marketplace.tcc.usjt.br.marketplace.activity.triggerCadastro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal.InitialActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.helper.Base64Helper;
import marketplace.tcc.usjt.br.marketplace.model.Historico;
import marketplace.tcc.usjt.br.marketplace.model.Produto;
import marketplace.tcc.usjt.br.marketplace.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    // Atributos do Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference historicReference;
    private DatabaseReference defaultListReference;
    private DatabaseReference recommendationProfileReference;
    // Atributos da Activity
    private ProgressBar spinner;
    private EditText cadastro_nome;
    private EditText cadastro_sobrenome;
    private EditText cadastro_email;
    private EditText cadastro_senha;
    private EditText cadastro_cpf;
    private EditText cadastro_telefone;
    private EditText cadastro_rua;
    private EditText cadastro_numero;
    private EditText cadastro_bairro;
    private EditText cadastro_cidade;
    private EditText cadastro_estado;
    private EditText cadastro_cep;
    private AlertDialog.Builder dialog_complete;
    private AlertDialog.Builder dialog_error;
    private String error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Instanciando variáveis do firebase
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        historicReference = FirebaseConfig.getFirebase().child("historics");
        defaultListReference = FirebaseConfig.getFirebase().child("defaultRecomendationList");
        recommendationProfileReference = FirebaseConfig.getFirebase().child("recommendationProfiles");

        // Reconhece os elementos da view
        cadastro_nome      = (EditText)findViewById(R.id.cadastro_nome);
        cadastro_sobrenome = (EditText)findViewById(R.id.cadastro_sobrenome);
        cadastro_email     = (EditText)findViewById(R.id.cadastro_email);
        cadastro_senha     = (EditText)findViewById(R.id.cadastro_senha);
        cadastro_cpf       = (EditText)findViewById(R.id.cadastro_cpf);
        cadastro_telefone  = (EditText)findViewById(R.id.cadastro_telefone);
        cadastro_rua       = (EditText)findViewById(R.id.cadastro_rua);
        cadastro_numero    = (EditText)findViewById(R.id.cadastro_numero);
        cadastro_bairro    = (EditText)findViewById(R.id.cadastro_bairro);
        cadastro_cidade    = (EditText)findViewById(R.id.cadastro_cidade);
        cadastro_estado    = (EditText)findViewById(R.id.cadastro_estado);
        cadastro_cep       = (EditText)findViewById(R.id.cadastro_cep);
        spinner            = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        // Definição das mascaras dos inputs
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN.NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskEstado = new SimpleMaskFormatter("LL");
        SimpleMaskFormatter simpleMaskCep = new SimpleMaskFormatter("NNNNN-NNN");
        //Coloca um lintener nos inputs que precisam de máscara
        MaskTextWatcher maskCpf = new MaskTextWatcher(cadastro_cpf, simpleMaskCpf);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(cadastro_telefone, simpleMaskTelefone);
        MaskTextWatcher maskEstado = new MaskTextWatcher(cadastro_estado, simpleMaskEstado);
        MaskTextWatcher maskCep = new MaskTextWatcher(cadastro_cep, simpleMaskCep);
        // Adiciona a máscara
        cadastro_cpf.addTextChangedListener(maskCpf);
        cadastro_telefone.addTextChangedListener(maskTelefone);
        cadastro_estado.addTextChangedListener(maskEstado);
        cadastro_cep.addTextChangedListener(maskCep);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    // Método de cadastro de usuário
    public void registerUser(View view) {
        spinner.setVisibility(View.VISIBLE);
        // Cria uma relação do usuário no Firebase 
        firebaseAuth.createUserWithEmailAndPassword(cadastro_email.getText().toString(),cadastro_senha.getText().toString())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
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
        user.setNome(cadastro_nome.getText().toString());
        user.setSobrenome(cadastro_sobrenome.getText().toString());
        user.setEmail(cadastro_email.getText().toString());
        user.setSenha(cadastro_senha.getText().toString());
        user.setCpf(cadastro_cpf.getText().toString());
        user.setTelefone(cadastro_telefone.getText().toString());
        user.setRua(cadastro_rua.getText().toString());
        user.setNumero(cadastro_numero.getText().toString());
        user.setBairro(cadastro_bairro.getText().toString());
        user.setCidade(cadastro_cidade.getText().toString());
        user.setEstado(cadastro_estado.getText().toString());
        user.setCep(cadastro_cep.getText().toString());
        user.save();

        String telefone = user.getTelefone();
        telefone.replace(" ", "");
        telefone.replace("-", "");
        telefone.replace("(", "");
        telefone.replace(")", "");
        String mensagem = "Bem vindo ao Market Place " + user.getNome() + " " + user.getSobrenome() + " !";
        sendSMS(telefone, mensagem);
        createdDefaultRecomendationProfile(userFirebase);
        createdDefaultHistoric(userFirebase);
    }

    private boolean sendSMS(String tel, String msg){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tel, null, msg, null, null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void createdDefaultHistoric(FirebaseUser userFirebase){
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        String fullDate = dateFormat.format(currentLocalTime);
        SimpleDateFormat date = new SimpleDateFormat("dd:MM:yyyy");
        String formatedDate = date.format(currentLocalTime);
        SimpleDateFormat hour = new SimpleDateFormat("hh:mm:ss");
        String formatedHour = hour.format(currentLocalTime);

        String sequence = Base64Helper.codifyBase64("historico " + fullDate);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Historico historic = new Historico();
        historic.setId(userFirebase.getUid());
        historic.setCompra(Base64Helper.codifyBase64("cart "+ user.getEmail() +  fullDate));
        historic.setData(formatedDate);
        historic.setHora(formatedHour);
        historic.setOrder("0");
        Log.i("teste", sequence);
        historic.save(Base64Helper.codifyBase64("cart "+ user.getEmail() +  fullDate));
    }

    public void createdDefaultRecomendationProfile(final FirebaseUser userFirebase){
        defaultListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                recommendationProfileReference.child(userFirebase.getUid().toString()).child(produto.getId()).setValue(produto);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public void createErrorDialog(String error_message){
        dialog_error = new AlertDialog.Builder(CadastroActivity.this);
        dialog_error.setTitle("Erro!");
        dialog_error.setCancelable(false);
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
        dialog_complete = new AlertDialog.Builder(CadastroActivity.this);
        dialog_complete.setTitle("Sucesso!");
        dialog_complete.setMessage("Seu perfil foi cadastrado com sucesso");
        dialog_complete.setCancelable(false);
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

    // Método de roteamento para a view inicial
    public void goToHomePage(){
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    // Método que limpa os valores do formulário
    public void clearValues(){
        cadastro_nome.setText("");
        cadastro_sobrenome.setText("");
        cadastro_email.setText("");
        cadastro_senha.setText("");
        cadastro_cpf.setText("");
        cadastro_telefone.setText("");
        cadastro_rua.setText("");
        cadastro_numero.setText("");
        cadastro_bairro.setText("");
        cadastro_cidade.setText("");
        cadastro_estado.setText("");
        cadastro_cep.setText("");
    }
}