package marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheCategoriaActivity;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheProdutoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.triggerInitial.MainActivity;
import marketplace.tcc.usjt.br.marketplace.adapter.CardExtremeAdapter;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.fragment.CategoriaFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.CriarMinhaListaFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.EntrarEmContatoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.HistoricoDeComprasFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.InitialFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.MinhaMelhorOpcaoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.PromocaoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.SobreFragment;
import marketplace.tcc.usjt.br.marketplace.model.Produto;
import marketplace.tcc.usjt.br.marketplace.model.Usuario;

public class InitialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CarouselView carouselView;
    private TextView userName;
    private TextView userEmail;
    private CircleImageView userPhoto;
    private ListView optionList;
    private Toolbar toolbar;
    private ProgressBar spinner;

    private Activity context;
    private Bundle params;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference productsReference;
    private String queryOption;

    private int[] sampleImages;
    private String bar;


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_nav);
        context = this;
        // Recupera o usuário atualmente logado
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.i("USUARIO_LOGADO",user.getEmail().toString());
            Log.i("USUARIO_LOGADO",user.getUid().toString());

            reference = FirebaseConfig.getFirebase().child("users").child(user.getUid().toString());
            // Cria o nó de recomendação caso não exista
            reference.child("recommendationProfiles").child(user.getUid().toString());
            retrieveUserData(reference);
        } else {
            Log.i("USUARIO_NAO_ENCONTRADO", "Erro");
        }

        spinner = (ProgressBar) findViewById(R.id.progressBar9);

        sampleImages = new int[]{R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};

        // Carrossel
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Header da sidenav
        View header = navigationView.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.user_name_initial);
        userEmail= (TextView) header.findViewById(R.id.user_email_initial);

        userPhoto= (CircleImageView) header.findViewById(R.id.imagem_de_perfil);
        userPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent profile = new Intent(context, PerfilActivity.class);
                startActivity(profile);
                return false;
            }
        });

        displaySelectedScreen(R.id.nav_home);

        // Cria uma referência a tabela de recomendação de produtos
        productsReference = FirebaseConfig.getFirebase().child("recommendationProfiles");
        productsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid().toString())) {
                    // Quando o usuário possui perfil de recomendação
                    queryOption = user.getUid().toString();
                    productsReference = productsReference.child(queryOption);
                    queryProfiles(productsReference);
                }
                else {
                    // Quando o usuário NÃO possui perfil de recomendação
                    queryOption = "default";
                    productsReference = productsReference.child(queryOption);
                    queryProfiles(productsReference);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void retrieveUserData(DatabaseReference reference){
        // Cria uma referência a tabela de recomendação de produtos
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                userName.setText(usuario.getNome().toString() + " " + usuario.getSobrenome().toString());
                userEmail.setText(usuario.getEmail().toString());
                if (usuario.getImagemDePerfil() != null) {
                    Picasso.with(context).load(usuario.getImagemDePerfil()).into(userPhoto);
                } else {
                    Picasso.with(context).load(R.drawable.person_placeholder).into(userPhoto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_market_car, menu);
        return true;
    }

    //Método para quando clicar nos itens de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_shopping:
                Intent dialer = new Intent(this, CarrinhoActivity.class);
                startActivity(dialer);
                return true;
        }
        return onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    public void displaySelectedScreen (int id){
        // Handle navigation view item clicks here.

        if (id == R.id.nav_home) {
            InitialFragment fragment = new InitialFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Início";
            carouselView.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_best_choice) {
            MinhaMelhorOpcaoFragment fragment = new MinhaMelhorOpcaoFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Minha melhor opção";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_my_list) {
            CriarMinhaListaFragment fragment = new CriarMinhaListaFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Criar minha lista";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_promotions) {
            PromocaoFragment fragment = new PromocaoFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Promoções";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_history) {
            HistoricoDeComprasFragment fragment = new HistoricoDeComprasFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Histórico de compras";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_contact) {
            EntrarEmContatoFragment fragment = new EntrarEmContatoFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Entrar em contato";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_about) {
            SobreFragment fragment = new SobreFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
            bar = "Sobre";
            carouselView.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_logout){
            firebaseAuth = FirebaseConfig.getFirebaseAuth();
            firebaseAuth.signOut();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            carouselView.setVisibility(View.INVISIBLE);
        }

        toolbar.setTitle(bar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void queryProfiles(DatabaseReference reference){
        // Estruturando a lista
        final ArrayList<Produto> list = new ArrayList<>();
        final CardExtremeAdapter adapter = new CardExtremeAdapter(list, context);
        optionList = (ListView) findViewById(R.id.lista_extreme);
        optionList.setAdapter(adapter);
        optionList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        optionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                params = new Bundle();
                params.putString("nomeProduto", list.get(position).getNome().toString());

                // Passa o nome do produto para a view de detalhe
                Intent detalheProduto = new Intent(context, DetalheProdutoActivity.class);
                detalheProduto.putExtras(params);
                startActivity(detalheProduto);
            }
        });

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

    public void openAllCategoriesList(View view) {
        CategoriaFragment fragment = new CategoriaFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.layout_for_fragment, fragment, fragment.getTag()).commit();
        bar = "Categorias";
        carouselView.setVisibility(View.INVISIBLE);
        toolbar.setTitle(bar);
    }

    public void openAcougueCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Açougue");
        products.putExtras(params);
        startActivity(products);
    }

    public void openBebidasCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Bebidas");
        products.putExtras(params);
        startActivity(products);
    }

    public void openHortifrutiCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Hortifruti");
        products.putExtras(params);
        startActivity(products);
    }

    public void openLimpezaCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Produtos de limpeza");
        products.putExtras(params);
        startActivity(products);
    }

    public void openLaticiniosCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Laticínios");
        products.putExtras(params);
        startActivity(products);
    }

    public void openAdegaCategory(View view) {
        Intent products = new Intent(this, DetalheCategoriaActivity.class);
        params = new Bundle();
        params.putString("nomeCategoria", "Adega");
        products.putExtras(params);
        startActivity(products);
    }
}
