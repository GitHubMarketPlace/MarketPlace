package marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.adapter.RemoveItemAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class CarrinhoActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private ListView lista_carrinho;
    private TextView titulo_carrinho;
    private RemoveItemAdapter adapter;
    private final ArrayList<Produto> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
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

        titulo_carrinho = (TextView) findViewById(R.id.titulo_carrinho);

        lista_carrinho = (ListView) findViewById(R.id.lista_carrinho);
        adapter = new RemoveItemAdapter(products, CarrinhoActivity.this);
        lista_carrinho.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                products.add(produto);
                titulo_carrinho.setText("Quantidade de produtos: " + products.size());
                adapter.notifyDataSetChanged();
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
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
