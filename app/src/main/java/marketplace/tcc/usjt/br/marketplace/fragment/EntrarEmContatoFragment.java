package marketplace.tcc.usjt.br.marketplace.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import marketplace.tcc.usjt.br.marketplace.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntrarEmContatoFragment extends Fragment {

    private View view;
    private Activity context;

    public EntrarEmContatoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_entrar_em_contato, container, false);
        context = getActivity();

        return view;
    }

}
