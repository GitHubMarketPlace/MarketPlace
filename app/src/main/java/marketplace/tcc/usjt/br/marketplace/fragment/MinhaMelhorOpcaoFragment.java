package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheProdutoActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.ProdutoCategoriaAdapter;
import marketplace.tcc.usjt.br.marketplace.adapter.RemoveItemAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;


/**
 * A simple {@link Fragment} subclass.
 */
public class MinhaMelhorOpcaoFragment extends Fragment {

    // Android view
    private ListView optionList;
    private ProgressBar spinner;
    private FloatingActionButton addCar;
    private FloatingActionButton editList;
    // Android
    private View view;
    private Activity context;
    private ArrayAdapter adapter;
    private Bundle params;
    private AlertDialog.Builder dialog_cart;
    private AlertDialog.Builder dialog_cart_2;
    private AlertDialog.Builder dialog_cart_3;
    private AlertDialog.Builder dialog_success;
    private AlertDialog.Builder dialog_success_2;
    private final ArrayList<Produto> list = new ArrayList<>();
    public boolean hasNormalAdapter = true;
    // Firebase
    private DatabaseReference reference;
    private DatabaseReference cart_reference;
    private FirebaseUser user;
    private Query queryRef;
    private String queryOption;


    public MinhaMelhorOpcaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_minha_melhor_opcao, container, false);
        context = getActivity();
        spinner = (ProgressBar) view.findViewById(R.id.progressBar1);

        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());
            cart_reference = FirebaseConfig.getFirebase().child("carts").child(user.getUid());
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        // Float de adicionar ao carrinho
        addCar = (FloatingActionButton) view.findViewById(R.id.fab_add_car);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToCart();
            }
        });

        // Float de editar lista de recomendações
        editList = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNormalAdapter == true){
                    final RemoveItemAdapter adapter = new RemoveItemAdapter(list, context);
                    optionList.setAdapter(adapter);
                    hasNormalAdapter = false;
                }else if(hasNormalAdapter == false){
                    final ProdutoCategoriaAdapter adapter = new ProdutoCategoriaAdapter(list, context);
                    optionList.setAdapter(adapter);
                    hasNormalAdapter = true;
                }
            }
        });

        // Cria uma referência a tabela de recomendação de produtos
        reference = FirebaseConfig.getFirebase().child("recommendationProfiles");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid().toString())) {
                    // Quando o usuário possui perfil de recomendação
                    queryOption = user.getUid().toString();
                    reference = reference.child(queryOption);
                    queryProfiles(reference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return view;
    }

    public void queryProfiles(DatabaseReference reference){
        // Estruturando a lista
        final ProdutoCategoriaAdapter adapter = new ProdutoCategoriaAdapter(list, context);
        optionList = (ListView) view.findViewById(R.id.lista_melhor_opcao);
        optionList.setAdapter(adapter);
        hasNormalAdapter = true;

        // Clique normal no item da lista
        optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // Clique longo no item da lista
        optionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(hasNormalAdapter == true){
                    createSuccessDialog();
                    createPositiveDialog(position);
                    dialog_cart.show();
                }
                return true;
            }
        });

        // Clique longo no item da lista
        optionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                createSuccessDialog();
                createPositiveDialog(position);
                dialog_cart.show();
                return true;
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
        dialog_cart = new AlertDialog.Builder(context);
        dialog_cart.setTitle("Deseja adicionar o produto ao carrinho?");
        dialog_cart.setCancelable(true);
        dialog_cart.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cart_reference.child(list.get(position).getNome()).setValue(list.get(position));
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

    public void sendToCart(){
        dialog_cart_2 = new AlertDialog.Builder(context);
        dialog_cart_2.setTitle("Deseja adicionar sua lista ao carrinho?");
        dialog_cart_2.setMessage("Toda lista será adicionada ao carrinho");
        dialog_cart_2.setCancelable(true);
        dialog_cart_2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Produto product = new Produto();
                if (list.size() > 0){
                    for (int i = 0; i < list.size(); i ++){
                        cart_reference.child(list.get(i).getNome()).setValue(list.get(i));
                    }
                    Toast.makeText(context, list.size() + " Produtos adicionados com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Insira produtos na lista!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_cart_2.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog_cart_2.create();
        dialog_cart_2.show();
    }
}
