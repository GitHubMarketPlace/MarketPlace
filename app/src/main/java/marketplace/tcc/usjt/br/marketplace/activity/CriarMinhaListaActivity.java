package marketplace.tcc.usjt.br.marketplace.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import marketplace.tcc.usjt.br.marketplace.R;

public class CriarMinhaListaActivity extends AppCompatActivity {

    private Toolbar toolbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_minha_lista);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_market_back, menu);
        return true;
    }
}
