package marketplace.tcc.usjt.br.marketplace.activity.triggerCategory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
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
        reference = FirebaseConfig.getFirebase().child("categories");
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, list);
        categoryList = (ListView)findViewById(R.id.lista_categorias);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cria uma interface bundle (tipo hashmap) para passar o nome da categoria para o intent
                params = new Bundle();
                params.putString("nomeCategoria", list.get(position));
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
                list.add(categoria.getNome());
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
}
