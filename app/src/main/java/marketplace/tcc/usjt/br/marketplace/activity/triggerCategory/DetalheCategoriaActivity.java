package marketplace.tcc.usjt.br.marketplace.activity.triggerCategory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class DetalheCategoriaActivity extends AppCompatActivity {

    private TextView nomeCategoria;
    private TextView labelQuatidade;
    private ProgressBar spinner;
    private ListView productList;
    private Activity context;
    private DatabaseReference reference;
    private Query queryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_categoria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        nomeCategoria = (TextView) findViewById(R.id.nome_categoria);
        spinner = (ProgressBar)findViewById(R.id.progressBar4);

        // Estrutura da lista de produtos
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, list);
        productList = (ListView)findViewById(R.id.lista_produtos_categoria);
        productList.setAdapter(adapter);

        // Verifica os dados vindos do intent de Categorias
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                String nome = params.getString("nomeCategoria");
                // Seta o nome da categoria no TextView
                nomeCategoria.setText(nome);

                reference = FirebaseConfig.getFirebase().child("products");
                queryRef =  reference.orderByChild("categoria").equalTo(nome);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        spinner.setVisibility(View.VISIBLE);
                        Produto produto = dataSnapshot.getValue(Produto.class);
                        list.add(produto.getNome());
                        adapter.notifyDataSetChanged();
                        spinner.setVisibility(View.GONE);

                        // Trata o TextView para mostrar a quantidade de produtos
                        labelQuatidade = (TextView) findViewById(R.id.label_quantidade);
                        String quantidade = "Quantidade de produtos: " + list.size();
                        labelQuatidade.setText(quantidade);
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
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
