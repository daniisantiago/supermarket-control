package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuSenha extends AppCompatActivity {

    private EditText EmailRedefinir;
    private Button recuperarSenha;
    private ImageButton imgVoltar;

    FirebaseAuth firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        firebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        EmailRedefinir = (EditText)findViewById(R.id.EmailRecuperarSenha);

        recuperarSenha = (Button)findViewById(R.id.btn_recuperar_senha);

        recuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmailRedefinir.getText().length()> 0){
                    String email = String.valueOf(EmailRedefinir.getText());

                    firebase.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("TAG", "E-mail para redefinição de senha enviado!");
                                Toast.makeText(EsqueceuSenha.this, "Verifique seu e-mail para redefinir sua senha!", Toast.LENGTH_LONG).show();
                                voltarLogin();
                            }else{
                                Toast.makeText(EsqueceuSenha.this, "E-mail inválido!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(EsqueceuSenha.this, "Digite o seu e-mail no campo acima!", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgVoltar = (ImageButton)findViewById(R.id.imageVoltarLogin);
        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarLogin();
            }
        });
    }


    private void voltarLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
