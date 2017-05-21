package marketplace.tcc.usjt.br.marketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class ProdutoPromocaoAdapter extends BaseAdapter {

    private final ArrayList<Produto> produtos;
    private final Activity context;

    public ProdutoPromocaoAdapter(ArrayList<Produto> produtos, Activity context) {
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
        view = inflater.inflate(R.layout.item_lista_produto_promocao, parent, false);

        Produto produto = produtos.get(position);

        CircleImageView fotoProduto = (CircleImageView) view.findViewById(R.id.item_lista_produto_imagem);
        TextView nomeProduto = (TextView)view.findViewById(R.id.item_lista_produto_nome);
        TextView valorProduto = (TextView)view.findViewById(R.id.item_lista_produto_preco);

        String colorText= "De " + "<font color=\"#E72A02\"><bold>" + "R$ " + produto.getValor().toString() + "</bold></font>" + " por " + "<font color=\"#159800\"><bold>" + "R$ " + produto.getValor_promocao().toString() + "</bold></font>";

        nomeProduto.setText(produto.getNome().toString());
        valorProduto.setText(Html.fromHtml(colorText));
        if (produto.getImagemProduto() != null) {
            Picasso.with(context).load(produto.getImagemProduto()).into(fotoProduto);
        } else {
            fotoProduto.setImageResource(R.drawable.ic_star);
        }

        return view;
    }
}
