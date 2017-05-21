package marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal.CarrinhoActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class DetalheProdutoActivity extends AppCompatActivity {

    private TextView nomeProduto;
    private ProgressBar spinner;
    private DatabaseReference reference;
    private Query queryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeProduto = (TextView) findViewById(R.id.nome_categoria);
        spinner = (ProgressBar)findViewById(R.id.progressBar4);

        // Verifica os dados vindos do intent de Categorias
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                String nome = params.getString("nomeProduto");

                Log.i("NOMEDOFDP", nome);
                reference = FirebaseConfig.getFirebase().child("products");
                queryRef =  reference.orderByChild("nome").equalTo(nome);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        spinner.setVisibility(View.VISIBLE);
                        Produto produto = dataSnapshot.getValue(Produto.class);

                        nomeProduto.setText(produto.getNome().toString());

                        spinner.setVisibility(View.GONE);
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_shopping:
                Intent dialer= new Intent(this, CarrinhoActivity.class);
                startActivity(dialer);
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_market_car, menu);
        return true;
    }
}
