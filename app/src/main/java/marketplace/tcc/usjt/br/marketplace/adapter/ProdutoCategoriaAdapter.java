package marketplace.tcc.usjt.br.marketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.model.Produto;


public class ProdutoCategoriaAdapter extends BaseAdapter {

    private final ArrayList<Produto> produtos;
    private final Activity context;
    private FirebaseStorage storage;
    private CircleImageView fotoProduto;
    private TextView nomeProduto;
    private TextView valorProduto;

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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_lista_produto_categoria, parent, false);

        Produto produto = produtos.get(position);
        fotoProduto = (CircleImageView) view.findViewById(R.id.item_lista_produto_imagem);
        nomeProduto = (TextView) view.findViewById(R.id.item_lista_produto_nome);
        valorProduto = (TextView) view.findViewById(R.id.item_lista_produto_preco);

        nomeProduto.setText(produto.getNome().toString());
        valorProduto.setText("Valor: R$ " + produto.getValor().toString());
        if (produto.getImagemProduto() != null) {
            Picasso.with(context).load(produto.getImagemProduto()).into(fotoProduto);
        } else {
            fotoProduto.setImageResource(R.drawable.ic_star);
        }

        return view;
    }
}
//    public void catchImageFromStorage(final Produto produto, View view){
//        // Cria uma referência ao storage do firebase
//        storage = FirebaseStorage.getInstance();
//        String folder = produto.getCategoria().toString();
//        String arquive = produto.getNome() + ".jpg";
//
//        StorageReference storageReference = storage
//                .getReferenceFromUrl("gs://marketplace-ed850.appspot.com")
//                .child(folder)
//                .child(arquive);
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {}
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                if (bitmap != null){
//                    nomeProduto.setText(produto.getNome().toString());
//                    valorProduto.setText("Valor: R$ " + produto.getValor().toString());
//                    fotoProduto.setImageBitmap(bitmap);
//                } else{
//                    nomeProduto.setText(produto.getNome().toString());
//                    valorProduto.setText("Valor: R$ " + produto.getValor().toString());
//                }
//
//            }
//        });
//    }

