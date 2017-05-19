package marketplace.tcc.usjt.br.marketplace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.activity.triggerInitial.MainActivity;

public class SplashScreenActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent();
                intent.setClass(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}
