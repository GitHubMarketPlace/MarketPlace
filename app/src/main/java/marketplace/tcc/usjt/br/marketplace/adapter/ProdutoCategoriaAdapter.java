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
import marketplace.tcc.usjt.br.marketplace.model.Produto;


public class ProdutoCategoriaAdapter extends BaseAdapter {

    private final ArrayList<Produto> produtos;
    private final Activity context;

    public ProdutoCategoriaAdapter(ArrayList<Produto> produtos, Activity context) {
        this.produtos = produtos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_lista_categoria, parent, false);

        Produto produto = produtos.get(position);

        CircleImageView fotoProduto = (CircleImageView) view.findViewById(R.id.item_lista_produto_imagem);
        TextView nomeProduto = (TextView)view.findViewById(R.id.item_lista_produto_nome);
        TextView valorProduto = (TextView)view.findViewById(R.id.item_lista_produto_preco);


        nomeProduto.setText(produto.getNome().toString());
        valorProduto.setText(produto.getValor().toString());
        fotoProduto.setImageResource(R.drawable.ic_star);

        return view;
    }
}
