package marketplace.tcc.usjt.br.marketplace.activity.triggerInitial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import marketplace.tcc.usjt.br.marketplace.activity.CarrinhoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.CriarMinhaListaActivity;
import marketplace.tcc.usjt.br.marketplace.activity.EntrarEmContatoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.HistoricoDeComprasActivity;
import marketplace.tcc.usjt.br.marketplace.activity.MinhaMelhorOpcaoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.PromocaoActivity;
import marketplace.tcc.usjt.br.marketplace.activity.SobreActivity;
import marketplace.tcc.usjt.br.marketplace.activity.triggerCategory.CategoriasActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;

public class InitialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private Activity context;
    CarouselView carouselView;
    int[] sampleImages = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4,
            R.drawable.image_5
    };
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
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent perfil = new Intent(this, InitialActivity.class);
            startActivity(perfil);
        } else if (id == R.id.nav_best_choice) {
            Intent myBestOption = new Intent(this, MinhaMelhorOpcaoActivity.class);
            startActivity(myBestOption);
        } else if (id == R.id.nav_my_list) {
            Intent createMyList = new Intent(this, CriarMinhaListaActivity.class);
            startActivity(createMyList);
        } else if (id == R.id.nav_promotions) {
            Intent promotion = new Intent(this, PromocaoActivity.class);
            startActivity(promotion);
        } else if (id == R.id.nav_history) {
            Intent history = new Intent(this, HistoricoDeComprasActivity.class);
            startActivity(history);
        } else if (id == R.id.nav_contact) {
            Intent contact = new Intent(this, EntrarEmContatoActivity.class);
            startActivity(contact);
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, SobreActivity.class);
            startActivity(about);
        } else if (id == R.id.nav_logout){
            firebaseAuth = FirebaseConfig.getFirebaseAuth();
            firebaseAuth.signOut();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openAllCategoriesList(View view) {
        Intent categories = new Intent(this, CategoriasActivity.class);
        startActivity(categories);
    }
}