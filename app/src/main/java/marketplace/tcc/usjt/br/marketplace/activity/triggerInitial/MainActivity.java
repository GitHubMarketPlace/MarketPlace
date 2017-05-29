package marketplace.tcc.usjt.br.marketplace.activity.triggerInitial;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerGlobal.InitialActivity;
import marketplace.tcc.usjt.br.marketplace.config.FirebaseConfig;
import marketplace.tcc.usjt.br.marketplace.helper.Permission;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String[] permissions = new String[]{
            android.Manifest.permission.SEND_SMS,
            Manifest.permission.BATTERY_STATS,
            Manifest.permission.INTERNET

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        Permission.validPermissions(1, this, permissions);

        verifyIfLoggedIn();
    }

    private void verifyIfLoggedIn(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            goToHomePage();
        }
    }

    public void goToHomePage(){
        Intent intent = new Intent(MainActivity.this, InitialActivity.class);
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

    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults){
            if (result == PackageManager.PERMISSION_DENIED){
                alertValidationPermission();
            }
        }
    }

    private void alertValidationPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar essa aplicação, é necessário aceitar as permissões.");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
