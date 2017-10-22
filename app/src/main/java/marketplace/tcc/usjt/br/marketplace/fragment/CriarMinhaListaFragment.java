package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheProdutoActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.ProdutoCategoriaAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class CriarMinhaListaFragment extends Fragment {

    private View view;
    private Query queryRef;
    private Activity context;
    private FirebaseUser user;
    private DatabaseReference cart_reference;
    private DatabaseReference user_cart_reference;
    private DatabaseReference products_reference;
    private Bundle params;
    private ListView listaProdutos;
    private CardView searchCard;
    private FloatingActionButton addCar;
    private AlertDialog.Builder dialog_cart;
    private AutoCompleteTextView autocomplete;
    private AlertDialog.Builder dialog_success;
    private ProdutoCategoriaAdapter adapter;
    // Lista de produtos adicionados
    private final ArrayList<String> list = new ArrayList<>();
    // Lista para o autocomplete
    private final ArrayList<Produto> products = new ArrayList<>();

    public CriarMinhaListaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_criar_minha_lista, container, false);
        context = getActivity();

        // Referência da Database
        products_reference = FirebaseConfig.getFirebase().child("products");
        cart_reference = FirebaseConfig.getFirebase().child("carts");

        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        //Criando Dialog de envio ao carrinho
        sendToCart();

        // Float de adicionar ao carrinho
        addCar = (FloatingActionButton) view.findViewById(R.id.fab_add_car_2);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cart.show();
            }
        });

        searchCard = (CardView) view.findViewById(R.id.card_search);
        searchCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autocomplete.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(autocomplete, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        // Autocomplete
        final ArrayAdapter<String> autocomplete_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, list);
        autocomplete = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);
        autocomplete.setAdapter(autocomplete_adapter);
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nome_produto =  autocomplete_adapter.getItem(position).toString();
                searchProductForList(nome_produto);
                autocomplete.clearFocus();
                hideSoftKeyboard(getActivity());
            }
        });

        // Lista de produtos
        listaProdutos = (ListView) view.findViewById(R.id.lista_produtos_selecionados);
        adapter = new ProdutoCategoriaAdapter(products, context);
        listaProdutos.setAdapter(adapter);
        listaProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        // Clique longo no item da lista
        listaProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                createSuccessDialog();
                createPositiveDialog(position);
                dialog_cart.show();
                return true;
            }
        });

        products_reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                list.add(produto.getNome());
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

        return view;
    }

    public void sendToCart(){
        dialog_cart = new AlertDialog.Builder(context);
        dialog_cart.setTitle("Deseja adicionar sua lista ao carrinho?");
        dialog_cart.setMessage("Toda lista será adicionada ao carrinho");
        dialog_cart.setCancelable(true);
        dialog_cart.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Produto product = new Produto();
                if (products.size() > 0){
                    for (int i = 0; i < products.size(); i ++){
                        cart_reference.child(user.getUid()).child(products.get(i).getNome()).setValue(products.get(i));
                    }
                    Toast.makeText(context, products.size() + " produtos adicionados ao carrinho", Toast.LENGTH_SHORT).show();
                    products.clear();
                    list.clear();
                }else{
                    Toast.makeText(context, "Insira produtos na lista!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_cart.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_cart.create();
    }

    public void searchProductForList(String nome_produto){
        queryRef =  products_reference.orderByChild("nome").equalTo(nome_produto);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                products.add(produto);
                adapter.notifyDataSetChanged();
                autocomplete.setText("");
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void createPositiveDialog(final int position){
        //Criando Dialog de envio ao carrinho
        dialog_cart = new AlertDialog.Builder(context);
        dialog_cart.setTitle("Deseja adicionar o produto ao carrinho?");
        dialog_cart.setCancelable(true);
        dialog_cart.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cart_reference.child(user.getUid()).child(products.get(position).getNome()).setValue(products.get(position));
                dialog_success.show();
            }
        });
        dialog_cart.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_cart.create();
    }

    public void createSuccessDialog(){
        //Criando Dialog de envio ao carrinho
        dialog_success = new AlertDialog.Builder(context);
        dialog_success.setTitle("Sucesso!");
        dialog_success.setMessage("O produto foi adicionado com sucesso ao carrinho");
        dialog_success.setCancelable(true);
        dialog_success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_success.create();
    }
}
