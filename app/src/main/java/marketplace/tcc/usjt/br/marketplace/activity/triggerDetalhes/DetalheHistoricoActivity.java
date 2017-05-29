package marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal.CarrinhoActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.ProdutoCategoriaAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class DetalheHistoricoActivity extends AppCompatActivity {

    private TextView historicoData;
    private TextView historicoHora;
    private TextView historicoQuantidade;
    private ProgressBar spinner;
    private ListView productList;
    private Activity context;
    private DatabaseReference reference;
    private Query queryRef;
    private String queryOption;
    private Bundle params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_historico);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        historicoData = (TextView)findViewById(R.id.historico_data);
        historicoHora = (TextView)findViewById(R.id.historico_hora);
        historicoQuantidade = (TextView)findViewById(R.id.historico_quantidade);

        spinner = (ProgressBar)findViewById(R.id.progressBar10);

        // Verifica os dados vindos do intent de Categorias
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                final String idCarrinho = params.getString("idCarrinho");
                final String dataCarrinho = params.getString("dataCarrinho");
                final String horaCarrinho = params.getString("horaCarrinho");

                historicoData.setText("Data: " + dataCarrinho);
                historicoHora.setText("Horario da compra: " + horaCarrinho);

                reference = FirebaseConfig.getFirebase().child("carts");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(idCarrinho)) {
                            // Quando o usuário possui perfil de recomendação
                            queryOption = idCarrinho;
                            reference = reference.child(queryOption);
                            queryProfiles(reference);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        }
    }

    public void queryProfiles(DatabaseReference reference){
        // Estrutura da lista de produtos
        final ArrayList<Produto> list = new ArrayList<>();
        final ProdutoCategoriaAdapter adapter = new ProdutoCategoriaAdapter(list, context);
        productList = (ListView)findViewById(R.id.lista_produtos_historico);
        productList.setAdapter(adapter);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cria uma interface bundle (tipo hashmap) para passar o nome doproduto para o intent
                params = new Bundle();
                params.putString("nomeProduto", list.get(position).getNome().toString());

                // Passa o nome do produto para a view de detalhe
                Intent detalheProduto = new Intent(context, DetalheProdutoActivity.class);
                detalheProduto.putExtras(params);
                startActivity(detalheProduto);
            }
        });

        // Listener (Query) para trazer os nomes das categorias
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                spinner.setVisibility(View.VISIBLE);
                Produto produto = dataSnapshot.getValue(Produto.class);
                list.add(produto);
                adapter.notifyDataSetChanged();
                spinner.setVisibility(View.GONE);

                String quantidade = "Quantidade de produtos: " + list.size();
                historicoQuantidade.setText(quantidade);
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


