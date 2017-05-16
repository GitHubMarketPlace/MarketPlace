package marketplace.tcc.usjt.br.marketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.model.Categoria;


public class CategoriaApapter extends BaseAdapter {
    private final ArrayList<Categoria> categorias;
    private final Activity context;

    public CategoriaApapter(ArrayList<Categoria> categorias ,Activity context) {
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public int getCount() {
        return categorias.size();
    }

    @Override
    public Object getItem(int position) {
        return categorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View recycledView, ViewGroup parent) {
        View view = recycledView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_lista_categoria, parent, false);

        Categoria categoria = categorias.get(position);

        CircleImageView fotoCategoria = (CircleImageView) view.findViewById(R.id.item_lista_categoria_imagem);
        TextView nomeCategoria = (TextView)view.findViewById(R.id.item_lista_categoria_nome);

        nomeCategoria.setText(categoria.getNome());
        fotoCategoria.setImageResource(R.drawable.ic_star);

        return view;
    }
}
