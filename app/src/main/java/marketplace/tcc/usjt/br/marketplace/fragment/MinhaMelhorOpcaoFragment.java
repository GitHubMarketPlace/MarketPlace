package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import marketplace.tcc.usjt.br.marketplace.adapter.ProdutoCategoriaAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;


/**
 * A simple {@link Fragment} subclass.
 */
public class MinhaMelhorOpcaoFragment extends Fragment {

    // Android view
    private ListView optionList;
    private ProgressBar spinner;
    // Android
    private View view;
    private Activity context;
    private ArrayAdapter adapter;
    private Bundle params;
    //Firebase
    private DatabaseReference reference;
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
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

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
                else {
                    // Quando o usuário NÃO possui perfil de recomendação
                    queryOption = "default";
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
        final ArrayList<Produto> list = new ArrayList<>();
        final ProdutoCategoriaAdapter adapter = new ProdutoCategoriaAdapter(list, context);
        optionList = (ListView) view.findViewById(R.id.lista_melhor_opcao);
        optionList.setAdapter(adapter);

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
}
