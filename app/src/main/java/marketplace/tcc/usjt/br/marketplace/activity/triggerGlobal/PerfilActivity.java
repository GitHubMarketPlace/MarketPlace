package marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Usuario;

public class PerfilActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference user_reference;
    private ProgressBar spinner;
    private CircleImageView userPhoto;
    private FloatingActionButton fab;
    private EditText usuario_nome;
    private EditText usuario_sobrenome;
    private EditText usuario_email;
    private EditText usuario_senha;
    private EditText usuario_cpf;
    private EditText usuario_telefone;
    private EditText usuario_rua;
    private EditText usuario_numero;
    private EditText usuario_bairro;
    private EditText usuario_cidade;
    private EditText usuario_estado;
    private EditText usuario_cep;
    private AlertDialog.Builder dialog_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());
            reference = FirebaseConfig.getFirebase().child("carts").child(user.getUid());
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        user_reference = FirebaseConfig.getFirebase().child("users").child(user.getUid().toString());
        userPhoto = (CircleImageView) findViewById(R.id.imagem_de_perfil);
        usuario_nome = (EditText)findViewById(R.id.usuario_nome);
        usuario_sobrenome = (EditText)findViewById(R.id.usuario_sobrenome);
        usuario_cpf = (EditText)findViewById(R.id.usuario_cpf);
        usuario_telefone  = (EditText)findViewById(R.id.usuario_telefone);
        usuario_rua = (EditText)findViewById(R.id.usuario_rua);
        usuario_numero = (EditText)findViewById(R.id.usuario_numero);
        usuario_bairro = (EditText)findViewById(R.id.usuario_bairro);
        usuario_cidade = (EditText)findViewById(R.id.usuario_cidade);
        usuario_estado = (EditText)findViewById(R.id.usuario_estado);
        usuario_cep = (EditText)findViewById(R.id.usuario_cep);
        spinner = (ProgressBar)findViewById(R.id.progressBar11);
        spinner.setVisibility(View.GONE);

        fab = (FloatingActionButton) findViewById(R.id.fab_refresh_data);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario model_user = new Usuario();
                            model_user.setId(user.getUid());
                            model_user.setNome(usuario_nome.getText().toString());
                            model_user.setSobrenome(usuario_sobrenome.getText().toString());
                            model_user.setCpf(usuario_cpf.getText().toString());
                            model_user.setTelefone(usuario_telefone.getText().toString());
                            model_user.setRua(usuario_rua.getText().toString());
                            model_user.setNumero(usuario_numero.getText().toString());
                            model_user.setBairro(usuario_bairro.getText().toString());
                            model_user.setCidade(usuario_cidade.getText().toString());
                            model_user.setEstado(usuario_estado.getText().toString());
                            model_user.setCep(usuario_cep.getText().toString());
                            model_user.save();

                            String telefone = model_user.getTelefone();
                            telefone.replace(" ", "");
                            telefone.replace("-", "");
                            telefone.replace("(", "");
                            telefone.replace(")", "");
                            String mensagem = "Dados do usuário " + usuario_nome.getText().toString() + " " + usuario_sobrenome.getText().toString() + " foram alterados no Market Place";
                            sendSMS(telefone, mensagem);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        // Definição das mascaras dos inputs
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN.NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskEstado = new SimpleMaskFormatter("LL");
        SimpleMaskFormatter simpleMaskCep = new SimpleMaskFormatter("NNNNN-NNN");
        //Coloca um lintener nos inputs que precisam de máscara
        MaskTextWatcher maskCpf = new MaskTextWatcher(usuario_cpf, simpleMaskCpf);
        MaskTextWatcher maskTelefone = new MaskTextWatcher(usuario_telefone, simpleMaskTelefone);
        MaskTextWatcher maskEstado = new MaskTextWatcher(usuario_estado, simpleMaskEstado);
        MaskTextWatcher maskCep = new MaskTextWatcher(usuario_cep, simpleMaskCep);
        // Adiciona a máscara
        usuario_cpf.addTextChangedListener(maskCpf);
        usuario_telefone.addTextChangedListener(maskTelefone);
        usuario_estado.addTextChangedListener(maskEstado);
        usuario_cep.addTextChangedListener(maskCep);

        // Cria uma referência a tabela de usuários
        reference = FirebaseConfig.getFirebase().child("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Se encontrar um nó igual ao id do usuario logado, retorna o usuário
                if (dataSnapshot.hasChild(user.getUid().toString())) {
                    // Trata a foto de perfil
                    if (dataSnapshot.child(user.getUid().toString()).hasChild("imagemDePerfil")) {
                        Picasso.with(PerfilActivity.this).load(dataSnapshot.child(user.getUid().toString()).child("imagemDePerfil").getValue().toString()).into(userPhoto);
                    } else {
                        Picasso.with(PerfilActivity.this).load(R.drawable.person_placeholder).into(userPhoto);
                    }

                    // Quando o usuário possui dados de perfil
                    usuario_nome.setText(dataSnapshot.child(user.getUid().toString()).child("nome").getValue().toString());
                    usuario_sobrenome.setText(dataSnapshot.child(user.getUid().toString()).child("sobrenome").getValue().toString());
                    usuario_cpf.setText(dataSnapshot.child(user.getUid().toString()).child("cpf").getValue().toString());
                    usuario_telefone.setText(dataSnapshot.child(user.getUid().toString()).child("telefone").getValue().toString());
                    usuario_rua.setText(dataSnapshot.child(user.getUid().toString()).child("rua").getValue().toString());
                    usuario_numero.setText(dataSnapshot.child(user.getUid().toString()).child("numero").getValue().toString());
                    usuario_bairro.setText(dataSnapshot.child(user.getUid().toString()).child("bairro").getValue().toString());
                    usuario_cidade.setText(dataSnapshot.child(user.getUid().toString()).child("cidade").getValue().toString());
                    usuario_estado.setText(dataSnapshot.child(user.getUid().toString()).child("estado").getValue().toString());
                    usuario_cep.setText(dataSnapshot.child(user.getUid().toString()).child("cep").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private boolean sendSMS(String tel, String msg){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tel, null, msg, null, null);
            spinner.setVisibility(View.GONE);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void createSuccessDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_success = new AlertDialog.Builder(PerfilActivity.this);
        dialog_success.setTitle("Dados alterados com sucesso!");
        dialog_success.setMessage("Faça login novamente no aplicativo para ver as alterações");
        dialog_success.setCancelable(true);
        dialog_success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog_success.create();
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

}
