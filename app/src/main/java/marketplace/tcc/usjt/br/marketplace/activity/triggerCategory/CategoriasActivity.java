package marketplace.tcc.usjt.br.marketplace.activity.triggerCategory;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.CarrinhoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheCategoriaActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.CategoriaApapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Categoria;

public class CategoriasActivity extends AppCompatActivity {

    private ListView categoryList;
    private ProgressBar spinner;
    private Activity context;
    private DatabaseReference reference;
    private Bundle params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        context = this;

        // Cria uma referência a tabela de categories
        reference = FirebaseConfig.getFirebase().child("categories");

        // Estruturando a lista
        final ArrayList<Categoria> list = new ArrayList<>();
        final CategoriaApapter adapter = new CategoriaApapter(list, this);
        categoryList = (ListView)findViewById(R.id.lista_categorias);
        categoryList.setAdapter(adapter);

        // Evento de clicar na lista
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Cria uma interface bundle (tipo hashmap) para passar o nome da categoria para o intent
                params = new Bundle();
                params.putString("nomeCategoria", list.get(position).getNome());

                // Passa o nome da categoria para a view de detalhe
                Intent detalheCategoria = new Intent(context, DetalheCategoriaActivity.class);
                detalheCategoria.putExtras(params);
                startActivity(detalheCategoria);
            }
        });

        spinner = (ProgressBar)findViewById(R.id.progressBar3);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                spinner.setVisibility(View.VISIBLE);
                Categoria categoria = dataSnapshot.getValue(Categoria.class);
                list.add(categoria);
                adapter.notifyDataSetChanged();
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_market_car, menu);
        return true;
    }

    //Método para quando clicar nos itens de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_shopping:
                Intent dialer= new Intent(this, CarrinhoActivity.class);
                startActivity(dialer);
                return true;
        }
        return onOptionsItemSelected(item);
    }
}
