package marketplace.tcc.usjt.br.marketplace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import marketplace.tcc.usjt.br.marketplace.R;

public class EstabelecimentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void cadastrarEstabelecimento(View view){
        Intent intent = new Intent(this, CadastroEstabelecimentoActivity.class);
        startActivity(intent);
    }

}
