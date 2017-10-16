package marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private AlertDialog.Builder dialog_cart;
    private AlertDialog.Builder dialog_success;
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
        titulo_carrinho.setText("Seu carrinho está vazio...");

        lista_carrinho = (ListView) findViewById(R.id.lista_carrinho);
        adapter = new RemoveItemAdapter(products, CarrinhoActivity.this);
        lista_carrinho.setAdapter(adapter);
        lista_carrinho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                createSuccessDialog();
                createPositiveDialog(position);
                dialog_cart.show();
                return false;
            }
        });

        loadCart();

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

    public void loadCart(){
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

    public void createPositiveDialog(final int position){
        //Criando Dialog de envio ao carrinho
        dialog_cart = new AlertDialog.Builder(CarrinhoActivity.this);
        dialog_cart.setTitle("Deseja remover o produdo do carrinho?");
        dialog_cart.setMessage("O produto será removido de sua lista");
        dialog_cart.setCancelable(true);
        dialog_cart.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String product_for_remove = products.get(position).getNome();
                reference.child(product_for_remove).removeValue();
                dialog_success.show();
            }
        });
        dialog_cart.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_cart.create();
    }

    public void createSuccessDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_success = new AlertDialog.Builder(CarrinhoActivity.this);
        dialog_success.setTitle("Sucesso!");
        dialog_success.setMessage("O produto foi removido do carrinho com sucesso");
        dialog_success.setCancelable(true);
        dialog_success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Limpa o array list e recarrega
                products.clear();
                loadCart();
            }
        });
        dialog_success.create();
    }

}
