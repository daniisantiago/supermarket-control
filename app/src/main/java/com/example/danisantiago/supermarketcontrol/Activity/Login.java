package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Usuario;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button cadastre, entrar;

    EditText editSenha, editEmail;

    private FirebaseAuth mAuth;
    private Usuario usuario;
    private TextView esqueceuSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        cadastre = (Button)findViewById(R.id.btnCadastro);
        entrar = (Button)findViewById(R.id.btnEntrar);

        editSenha = (EditText) findViewById(R.id.editSenha);
        editEmail = (EditText) findViewById(R.id.editEmail);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editSenha.getText().length() == 0 || editEmail.getText().length() == 0){
                    Toast.makeText(Login.this, "É necessário preencher todos os campos", Toast.LENGTH_LONG).show();
                }else{
                    String senha = editSenha.getText().toString();
                    String email = editEmail.getText().toString();
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    validarLogin();
                }
            }
        });

        cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CadastroUser.class);
                startActivity(intent);
                finish();
            }
        });

        esqueceuSenha = (TextView)findViewById(R.id.findSenha);

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, EsqueceuSenha.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validarLogin(){
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirHome();
                    Toast.makeText(Login.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Login.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void abrirHome(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }
}
