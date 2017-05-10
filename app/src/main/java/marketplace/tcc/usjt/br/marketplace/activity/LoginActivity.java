package marketplace.tcc.usjt.br.marketplace.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.helper.Base64Helper;
import marketplace.tcc.usjt.br.marketplace.helper.Preferences;
import marketplace.tcc.usjt.br.marketplace.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText user_email;
    private EditText user_password;
    private FirebaseAuth firebaseAuth;
    private ProgressBar spinner;
    private AlertDialog.Builder dialog_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Da suporte a barra de ações (seta de voltar ou infla um menu)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();

        // Encontra elementos no formulário
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);
        user_email = (EditText)findViewById(R.id.user_email);
        user_password = (EditText)findViewById(R.id.user_password);

        //Criando Dialog de confirmação
        dialog_error = new AlertDialog.Builder(LoginActivity.this);
        dialog_error.setTitle("Erro");
        dialog_error.setMessage("Verifique seus dados de acesso!");
        dialog_error.setCancelable(false);
        dialog_error.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        dialog_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_error.create();
    }

    // Seta de voltar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return onOptionsItemSelected(item);
    }

    // Seta de voltar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void login(View view){
        spinner.setVisibility(View.VISIBLE);
        if(user_email.getText().toString().equals("") || user_password.getText().toString().equals("")){
            dialog_error.show();
            spinner.setVisibility(View.GONE);
        } else {
            Usuario user = new Usuario();
            user.setEmail(user_email.getText().toString());
            user.setSenha(user_password.getText().toString());
            verifyLogin(user.getEmail(),user.getSenha());
        }
    }

    public void verifyLogin(final String email, String password){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Salva o e-mail do usuário em um aquivo no próprio aparelho
                            // Isso facilita no caso de precisarmos futuramente do dado em outro lugar
                            Preferences preferences = new Preferences(LoginActivity.this);
                            String identifyUser = Base64Helper.codifyBase64(email);
                            preferences.saveData(identifyUser);

                            clearValues();
                            goToHomePage();
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Bem vindo ao Market Place!", Toast.LENGTH_LONG).show();
                        }else{
                            spinner.setVisibility(View.GONE);
                            dialog_error.show();
                        }
                    }
                });
    }

    public void cadastrarUsuario(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void goToHomePage(){
        Intent intent = new Intent(LoginActivity.this, SideNavActivity.class);
        startActivity(intent);
        finish();
    }

    public void clearValues(){
        user_email.setText("");
        user_password.setText("");
    }
}
