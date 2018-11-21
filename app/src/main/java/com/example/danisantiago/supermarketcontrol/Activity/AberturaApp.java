package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;

public class AberturaApp extends AppCompatActivity {
    //Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura_app);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(usuarioFirebase.getCurrentUser() != null){
                    Intent home = new Intent(AberturaApp.this, Home.class);
                    startActivity(home);
                    finish();
                }else{
                    //Esse metodo será executado sempre que o timer acabar
                    Intent login = new Intent(AberturaApp.this, Login.class);
                    startActivity(login);

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);


    }



}
