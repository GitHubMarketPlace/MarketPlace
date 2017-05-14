package marketplace.tcc.usjt.br.marketplace.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class CategoriesActivity extends AppCompatActivity {

    private ListView categoryList;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        DatabaseReference reference = FirebaseConfig.getFirebase().child("categories");
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<Categoria> listTeste = new ArrayList<Categoria>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        categoryList = (ListView)findViewById(R.id.lista_categorias);
        categoryList.setAdapter(adapter);
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

        // TESTANDO PEGAR JSON PARA MANDAR PARA REDE NEURAL
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Pega todos os filhos de categories
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//
//                for (DataSnapshot child: children) {
//                    Categoria categoria = child.getValue(Categoria.class);
//                    listTeste.add(categoria);
//                }
//                Log.i("FIREBASE_INFO", String.valueOf(listTeste));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
}
