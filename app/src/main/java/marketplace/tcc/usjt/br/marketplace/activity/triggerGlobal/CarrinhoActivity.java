package marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheProdutoActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.RemoveItemAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.helper.Base64Helper;
import marketplace.tcc.usjt.br.marketplace.model.Historico;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class CarrinhoActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference close_cart_reference;
    private DatabaseReference historics_reference;
    private Bundle params;
    private Activity context;
    private ListView lista_carrinho;
    private TextView titulo_carrinho;
    private Double totalValue;
    private FloatingActionButton fabBuy;
    private FloatingActionButton clearCart;
    private RemoveItemAdapter adapter;
    private AlertDialog.Builder dialog_cart;
    private AlertDialog.Builder dialog_buy;
    private AlertDialog.Builder dialog_success;
    private AlertDialog.Builder dialog_buy_success;
    private AlertDialog.Builder dialog_clear;
    private AlertDialog.Builder dialog_clear_success;
    private final ArrayList<Produto> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = CarrinhoActivity.this;

        close_cart_reference = FirebaseConfig.getFirebase().child("carts");
        historics_reference = FirebaseConfig.getFirebase().child("historics");

        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());
            reference = FirebaseConfig.getFirebase().child("carts").child(user.getUid());
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        clearCart = (FloatingActionButton) findViewById(R.id.fab_clear);
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClearDialog();
            }
        });

        fabBuy = (FloatingActionButton) findViewById(R.id.fab_buy);
        fabBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalValue = 0.00;
                for (int i = 0; i < products.size(); i ++) {
                    totalValue += Double.parseDouble(products.get(i).getValor());
                }
                createBuyRequestDialog();
            }
        });

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
                return true;
            }
        });
        lista_carrinho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                params = new Bundle();
                params.putString("nomeProduto", products.get(position).getNome().toString());

                // Passa o nome do produto para a view de detalhe
                Intent detalheProduto = new Intent(context, DetalheProdutoActivity.class);
                detalheProduto.putExtras(params);
                startActivity(detalheProduto);
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

    public void finalizeCart(){
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        String fullDate = dateFormat.format(currentLocalTime);
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = date.format(currentLocalTime);
        SimpleDateFormat hour = new SimpleDateFormat("HH:mm:ss");
        String formatedHour = hour.format(currentLocalTime);
        final String formatedDateFirebase = "finalizado" + date.format(currentLocalTime) + "-" + hour.format(currentLocalTime) + user.getUid();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                String childFinalized = formatedDateFirebase;
                close_cart_reference.child(childFinalized).child(produto.getNome()).setValue(produto);
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

        close_cart_reference.child(user.getUid()).removeValue();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Historico historic = new Historico();
        historic.setId(user.getUid());
        historic.setCompra(Base64Helper.codifyBase64("cart "+ user.getEmail() +  fullDate));
        historic.setData(formatedDate);
        historic.setHora(formatedHour);
        historic.setOrder("0");
        historic.save(Base64Helper.codifyBase64("cart "+ user.getEmail() +  fullDate));
        createBuySuccessDialog();
    }

    public void createPositiveDialog(final int position){
        //Criando Dialog de envio ao carrinho
        dialog_cart = new AlertDialog.Builder(CarrinhoActivity.this);
        dialog_cart.setTitle("Deseja remover o produto do carrinho?");
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

    public void createBuyRequestDialog(){
        String message = "Valor total: R$" + totalValue;
        //Criando Dialog de finalização de compra
        dialog_buy = new AlertDialog.Builder(context);
        dialog_buy.setTitle("Deseja finalizar sua compra?");
        dialog_buy.setMessage(message);
        dialog_buy.setCancelable(true);
        dialog_buy.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (products.size() > 0){
                    finalizeCart();
                }else {
                    Toast.makeText(context, "Carrinho vazio! Adicione novos produtos para sua compra ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_buy.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_buy.create();
        dialog_buy.show();
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
                finish();
            }
        });
        dialog_success.create();
    }
    public void createBuySuccessDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_buy_success = new AlertDialog.Builder(CarrinhoActivity.this);
        dialog_buy_success.setTitle("Compra finalizada com sucesso!");
        dialog_buy_success.setMessage("Agredecemos a preferência");
        dialog_buy_success.setCancelable(true);
        dialog_buy_success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog_buy_success.create();
        dialog_buy_success.show();
    }

    public void createClearDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_clear = new AlertDialog.Builder(context);
        dialog_clear.setTitle("Deseja remover todos os produtos do carrinho?");
        dialog_clear.setCancelable(true);
        dialog_clear.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (products.size() > 0){
                    close_cart_reference.child(user.getUid()).removeValue();
                    createClearSuccessDialog();
                } else{
                    Toast.makeText(context, "Carrinho vazio! Adicione novos produtos para sua compra ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_clear.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_clear.create();
        dialog_clear.show();
    }

    public void createClearSuccessDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_clear_success = new AlertDialog.Builder(context);
        dialog_clear_success.setTitle("Sucesso!");
        dialog_clear_success.setMessage("Produtos removidos do carrinho");
        dialog_clear_success.setCancelable(true);
        dialog_clear_success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog_clear_success.create();
        dialog_clear_success.show();
    }

}
