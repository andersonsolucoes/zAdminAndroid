package br.com.unidosdacachorra.zadmin.inicio;

/**
 * Created by Anderson on 11/07/2016.
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.database.GerarDatabase;
import br.com.unidosdacachorra.zadmin.login.Credencial;
import br.com.unidosdacachorra.zadmin.login.LoginActivity;


public class Splash extends Activity {

    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GerarDatabase gerarBase = new GerarDatabase(getBaseContext());

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
                Boolean logado = shared.getBoolean("logado", false);
                Intent i;

                if(logado) {
                    i = new Intent(Splash.this, ActivityPrincipal.class);
                    Credencial.logado = Boolean.TRUE;
                    Credencial.login = shared.getString("login", "");
                    Credencial.user_name = shared.getString("user_name", "");
                    Credencial.email = shared.getString("email", "");
                } else {
                    i = new Intent(Splash.this, LoginActivity.class);
                }
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout); //animação aqui
                transaction.commit();

                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
