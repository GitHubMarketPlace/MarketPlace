package marketplace.tcc.usjt.br.marketplace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import marketplace.tcc.usjt.br.marketplace.R;

public class MinhaMelhorOpcaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_best_option);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_market_car, menu);
        return true;
    }

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
}
