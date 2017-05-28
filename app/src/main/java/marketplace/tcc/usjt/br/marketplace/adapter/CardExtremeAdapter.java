package marketplace.tcc.usjt.br.marketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class CardExtremeAdapter extends BaseAdapter{

        private final ArrayList<Produto> produtos;
        private final Activity context;
        private FirebaseStorage storage;
        private ImageView fotoProduto;
        private TextView valorProduto;
        private TextView descricaoProduto;

        public CardExtremeAdapter(ArrayList<Produto> produtos, Activity context) {
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

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_lista_extreme, parent, false);

            Produto produto = produtos.get(position);
            fotoProduto = (ImageView) view.findViewById(R.id.item_lista_extreme_imagem);
            valorProduto = (TextView) view.findViewById(R.id.item_lista_extreme_valor);
            descricaoProduto = (TextView) view.findViewById(R.id.item_lista_extreme_descricao);

            valorProduto.setText("R$ " + produto.getValor().toString());
            descricaoProduto.setText(produto.getDescricao().toString());
            if (produto.getImagemProduto() != null) {
                Picasso.with(context).load(produto.getImagemProduto()).into(fotoProduto);
            } else {
                fotoProduto.setImageResource(R.drawable.ic_star);
            }
            return view;
        }

}
