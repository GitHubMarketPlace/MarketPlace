package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import marketplace.tcc.usjt.br.marketplace.adapter.ProdutoCategoriaAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriarMinhaListaFragment extends Fragment {

    private View view;
    private Query queryRef;
    private Activity context;
    private FirebaseUser user;
    private DatabaseReference cart_reference;
    private DatabaseReference user_cart_reference;
    private DatabaseReference products_reference;

    private ListView listaProdutos;
    private FloatingActionButton addCar;
    private AlertDialog.Builder dialog_cart;
    private AutoCompleteTextView autocomplete;

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
        dialog_cart = new AlertDialog.Builder(context);
        dialog_cart.setTitle("Deseja adicionar sua lista ao carrinho?");
        dialog_cart.setMessage("Toda lista será adicionada ao carrinho");
        dialog_cart.setCancelable(true);
        dialog_cart.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Produto product = new Produto();
                if (products.size() > 0){
                    int count = 0;
                    for (int i = 0; i < products.size(); i ++){
                        cart_reference.child(user.getUid()).child(products.get(i).getNome()).setValue(products.get(i));
                        products.remove(i);
                        count ++;
                        if (count == products.size()){
                            Toast.makeText(context, "Produtos adicionados com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    }
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

        // Float de adicionar ao carrinho
        addCar = (FloatingActionButton) view.findViewById(R.id.fab_add_car_2);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_cart.show();
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
            }
        });

        // Lista de produtos
        listaProdutos = (ListView) view.findViewById(R.id.lista_produtos_selecionados);
        adapter = new ProdutoCategoriaAdapter(products, context);
        listaProdutos.setAdapter(adapter);

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

    public void searchProductForList(String nome_produto){
        queryRef =  products_reference.orderByChild("nome").equalTo(nome_produto);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Produto produto = dataSnapshot.getValue(Produto.class);
                products.add(produto);
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

}
