package marketplace.tcc.usjt.br.marketplace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        verifyIfLoggedIn();
    }

    private void verifyIfLoggedIn(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            goToHomePage();
        }
    }

    public void goToHomePage(){
        Intent intent = new Intent(MainActivity.this, SideNavActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void goToEstablishment(View view){
        Intent intent = new Intent(this, EstabelecimentoActivity.class);
        startActivity(intent);
    }
}
