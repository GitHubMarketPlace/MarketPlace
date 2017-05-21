package marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal.CarrinhoActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.model.Produto;

public class DetalheProdutoActivity extends AppCompatActivity {

    private TextView nomeProduto;
    private TextView nomeProprietario;
    private TextView precoProduto;
    private TextView precoProdutoPromocao;
    private TextView total;
    private ImageView fotoProduto;
    private ProgressBar spinner;
    private Spinner select;
    private CheckBox checkboxPromocao;
    private Activity context;

    private DatabaseReference reference;
    private Query queryRef;

    private Produto produto;
    private ArrayAdapter<String> adapter;
    private String valorFormatado;
    private String[] quantidades;
    private double valorTotalProduto;
    private double preco;
    private int quantidadeSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Definindo alguns atributos
        context = this;
        quantidades = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, quantidades);

        // Reconhece os elementos da view
        checkboxPromocao = (CheckBox) findViewById(R.id.checkbox_promocao);
        nomeProduto = (TextView) findViewById(R.id.nome_produto);
        nomeProprietario = (TextView) findViewById(R.id.nome_proprietario);
        precoProduto = (TextView) findViewById(R.id.preco_produto);
        precoProdutoPromocao = (TextView) findViewById(R.id.preco_produto_promocao);
        total = (TextView) findViewById(R.id.total_produto);
        fotoProduto = (ImageView) findViewById(R.id.foto_produto);
        spinner = (ProgressBar)findViewById(R.id.progressBar7);
        select = (Spinner) findViewById(R.id.select_quantidade);
        select.setAdapter(adapter);

        // Verifica os dados vindos do intent de Categorias
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                String nome = params.getString("nomeProduto");
                reference = FirebaseConfig.getFirebase().child("products");
                queryRef =  reference.orderByChild("nome").equalTo(nome);

                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        spinner.setVisibility(View.VISIBLE);
                        produto = dataSnapshot.getValue(Produto.class);
                        nomeProduto.setText(produto.getNome().toString());
                        nomeProprietario.setText(produto.getNome_proprietario().toString());
                        Picasso.with(context).load(produto.getImagemProduto()).into(fotoProduto);
                        precoProduto.setText("Valor unitário: R$ " + produto.getValor().toString());
                        precoProdutoPromocao.setText("Valor promocional: R$ " + produto.getValor_promocao().toString());
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

        // Evento de quando o checkbox for clicado
        checkboxPromocao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // Se o usuário usar o preco promocional
                    if(quantidadeSelecionada > 0){
                        preco = Double.parseDouble(produto.getValor_promocao());
                        valorTotalProduto = preco * quantidadeSelecionada;
                        valorFormatado = String.format("%.2f", valorTotalProduto);
                        total.setText("Total: R$ " + valorFormatado);
                    }else{
                        Toast.makeText(context, "Defina uma quantidade para o produto!", Toast.LENGTH_SHORT).show();
                        checkboxPromocao.toggle();
                    }
                }else{
                    preco = Double.parseDouble(produto.getValor());
                    valorTotalProduto = preco * quantidadeSelecionada;
                    valorFormatado = String.format("%.2f", valorTotalProduto);
                    total.setText("Total: R$ " + valorFormatado);
                }
            }
        });


        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Pega a quantidade selecionada
                quantidadeSelecionada = Integer.parseInt(quantidades[position]);
                if (produto !=  null){
                    if (checkboxPromocao.isChecked() == true){
                        // Se o usuário usar o preco promocional
                        valorTotalProduto = Double.parseDouble(produto.getValor_promocao()) * quantidadeSelecionada;
                    }else {
                        // Se o usuário usar o preco normal
                        valorTotalProduto = Double.parseDouble(produto.getValor()) * quantidadeSelecionada;
                    }
                    // Formatando casas decimais
                    String valorFormatado = String.format("%.2f", valorTotalProduto);
                    total.setText("Total: R$ " + valorFormatado);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_shopping:
                Intent dialer= new Intent(this, CarrinhoActivity.class);
                startActivity(dialer);
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_market_car, menu);
        return true;
    }

    public void addCar(View view) {
        if (valorTotalProduto > 0){
            Toast.makeText(context, "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Não foi possível adicionar o produto.", Toast.LENGTH_SHORT).show();
        }
    }
}
