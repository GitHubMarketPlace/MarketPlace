package marketplace.tcc.usjt.br.marketplace.activity.triggerInitial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerCadastro.CadastroEstabelecimentoActivity;

public class EstabelecimentoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
        // Da suporte a barra de ações (seta de voltar ou infla um menu)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void cadastrarEstabelecimento(View view){
        Intent intent = new Intent(this, CadastroEstabelecimentoActivity.class);
        startActivity(intent);
    }

}
