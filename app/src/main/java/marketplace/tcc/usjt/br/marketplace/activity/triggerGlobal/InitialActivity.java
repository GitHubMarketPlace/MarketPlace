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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerDetalhes.DetalheCategoriaActivity;
import marketplace.tcc.usjt.br.marketplace.activity.triggerInitial.MainActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.fragment.CategoriaFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.CriarMinhaListaFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.EntrarEmContatoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.HistoricoDeComprasFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.InitialFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.MinhaMelhorOpcaoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.PromocaoFragment;
import marketplace.tcc.usjt.br.marketplace.fragment.SobreFragment;

public class InitialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private Activity context;
    private CarouselView carouselView;
    private int[] sampleImages = { R.drawable.image_1,  R.drawable.image_2,  R.drawable.image_3,  R.drawable.image_4,  R.drawable.image_5 };
    private Bundle params;
    private String bar;
    private Toolbar toolbar;

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

        displaySelectedScreen(R.id.nav_home);

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
                Intent dialer= new Intent(this, CarrinhoActivity.class);
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
            bar = "Contato";
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