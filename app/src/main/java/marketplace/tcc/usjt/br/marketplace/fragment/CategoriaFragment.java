package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheCategoriaActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Categoria;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaFragment extends Fragment {

    private ListView categoryList;
    private ProgressBar spinner;
    private Activity context;
    private DatabaseReference reference;
    private Bundle params;
    private ArrayAdapter adapter;
    private View view;

    public CategoriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_categoria, container, false);
        context = getActivity();

        // Cria uma referência a tabela de categories
        reference = FirebaseConfig.getFirebase().child("categories");

        // Estruturando a lista
        final ArrayList<String> list = new ArrayList<>();
        //final CategoriaApapter adapter = new CategoriaApapter(list, this);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_selectable_list_item, list);
        categoryList = (ListView) view.findViewById(R.id.lista_categorias);
        categoryList.setAdapter(adapter);

        // Evento de clicar na lista
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

        spinner = (ProgressBar) view.findViewById(R.id.progressBar3);

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

        return view;
    }
}

