//package marketplace.tcc.usjt.br.marketplace.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import marketplace.tcc.usjt.br.marketplace.R;
//import marketplace.tcc.usjt.br.marketplace.model.Categoria;
//
///**
// * Created by olverajunior2014 on 20/03/17.
// */
//
//public class CategoriaApapter extends BaseAdapter {
//    Categoria[] categorias;
//    Activity context;
//
//    public CategoriaApapter(Activity context, Categoria[] categorias) {
//        this.context = context;
//        this.categorias = categorias;
//    }
//
//    @Override
//    public int getCount() {
//        return categorias.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        if(i >=0 && i < categorias.length) {
//            return categorias[i];
//        } else{
//            return null;
//        }
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View recycledView, ViewGroup viewGroup) {
//        View view = recycledView;
//        if(view == null){
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.item_category, viewGroup, false);
//            ImageView fotoCategoria = (ImageView)view.findViewById(R.id.foto_categoria);
//            TextView nomeCategoria = (TextView)view.findViewById(R.id.nome_categoria);
////            TextView detalheCategoria = (TextView)view.findViewById(R.id.detalhe_categoria);
////            view.setTag(new ViewHolder(detalheCategoria, fotoCategoria, nomeCategoria));
//        }
//
////        ViewHolder holder = (ViewHolder) view.getTag();
////        holder.getNomeCliente().setText(clientes[i].getNome());
////        holder.getDetalheCliente().setText(clientes[i].getFone() + " - " + clientes[i].getEmail() );
////
////        Drawable imagem = Util.getDrawable(context,clientes[i].getFoto());
////        holder.getFotoCliente().setImageDrawable(imagem);
//
//        return view;
//    }
//}
