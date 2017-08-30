package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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
    private Activity context;
    private DatabaseReference reference;
    private DatabaseReference product_reference;
    private Query queryRef;

    private ProgressBar spinner;
    private ListView listaProdutos;
    private Button adicionar;
    private AutoCompleteTextView autocomplete;

    private ProdutoCategoriaAdapter adapter;
    // Lista para o autocomplete
    private final ArrayList<Produto> products = new ArrayList<>();
    // Lista de produtos adicionados
    private final ArrayList<String> list = new ArrayList<>();

    public CriarMinhaListaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_criar_minha_lista, container, false);
        context = getActivity();

        // Referência da Database
        reference = FirebaseConfig.getFirebase().child("products");

        // Spinner
        spinner = (ProgressBar) view.findViewById(R.id.progressBar11);

        // Autocomplete
        ArrayAdapter<String> autocomplete_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, list);
        autocomplete = (AutoCompleteTextView) view.findViewById(R.id.autocomplete);
        autocomplete.setAdapter(autocomplete_adapter);

        // Lista de produtos
        listaProdutos = (ListView) view.findViewById(R.id.lista_produtos_selecionados);
        adapter = new ProdutoCategoriaAdapter(products, context);
        listaProdutos.setAdapter(adapter);

        //Evento de adicionar na lista
        adicionar = (Button) view.findViewById(R.id.adicionar);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                String product_name = autocomplete.getText().toString();
                queryRef =  reference.orderByChild("nome").equalTo(product_name);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Produto produto = dataSnapshot.getValue(Produto.class);
                        products.add(produto);
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
        });

        reference.addChildEventListener(new ChildEventListener() {
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

}
