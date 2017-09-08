package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheHistoricoActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.HistoricoAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Historico;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricoDeComprasFragment extends Fragment {

    // Android view
    private ListView historicList;
    private ProgressBar spinner;
    // Android
    private View view;
    private Activity context;
    private Bundle params;
    // Firebase
    private DatabaseReference reference;
    private FirebaseUser user;
    private Query queryRef;
    private String queryOption;

    public HistoricoDeComprasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_historico_de_compras, container, false);
        context = getActivity();
        spinner = (ProgressBar) view.findViewById(R.id.progressBar8);

        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        // Cria uma referência a tabela de histórico de produtos
        reference = FirebaseConfig.getFirebase().child("historics").child(user.getUid().toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryHistorics(reference);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        return view;
    }

    public void queryHistorics(DatabaseReference reference){
        // Estruturando a lista
        final ArrayList<Historico> list = new ArrayList<>();
        final HistoricoAdapter adapter = new HistoricoAdapter(list, context);
        historicList = (ListView) view.findViewById(R.id.lista_historico);
        historicList.setAdapter(adapter);
        historicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Cria uma interface bundle (tipo hashmap) para passar o nome da categoria para o intent
                params = new Bundle();
                params.putString("idCarrinho", list.get(position).getCompra().toString());
                params.putString("dataCarrinho", list.get(position).getData().toString());
                params.putString("horaCarrinho", list.get(position).getHora().toString());

                // Passa o nome da categoria para a view de detalhe
                Intent detalheHistorico = new Intent(context, DetalheHistoricoActivity.class);
                detalheHistorico.putExtras(params);
                startActivity(detalheHistorico);
            }
        });

        queryRef = reference.orderByChild("order");
        // Listener (Query) para trazer os nomes das categorias
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                spinner.setVisibility(View.VISIBLE);
                Historico historico = dataSnapshot.getValue(Historico.class);
                list.add(historico);
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
