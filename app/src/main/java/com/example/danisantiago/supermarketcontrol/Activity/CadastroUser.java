package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Usuario;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.Helper.Preferencias;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroUser extends AppCompatActivity{

    private static final String EMAIL_FORMATO =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_FORMATO, Pattern.CASE_INSENSITIVE);

    private EditText editCadNome, editCadSenha, editCadEmail, editCadConfirmSenha;
    private Button btnCad;
    ImageButton voltar;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editCadNome = (EditText) findViewById(R.id.editCadNome);
        editCadSenha = (EditText) findViewById(R.id.editCadSenha);
        editCadEmail = (EditText) findViewById(R.id.editCadEmail);
        editCadConfirmSenha = (EditText) findViewById(R.id.editCadConfimarSenha);

        btnCad = (Button) findViewById(R.id.btnCad);
        voltar = (ImageButton) findViewById(R.id.imageVoltar);

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editCadNome.getText().toString() == null || editCadEmail.getText().toString() == null){
                    Toast.makeText(CadastroUser.this , "Todos os campos devem ser preenchidos!", Toast.LENGTH_SHORT).show();
                }else{
                    if(validarEmail(editCadEmail.getText().toString()) == true){
                        if(editCadSenha.getText().length() < 6 || editCadConfirmSenha.getText().length()< 6){
                            String text = "A senha deve conter no minímo 6 digítos!";
                            Toast.makeText(CadastroUser.this , text, Toast.LENGTH_SHORT).show();
                        }else{
                            if(editCadSenha.getText().toString().equals(editCadConfirmSenha.getText().toString())){
                                usuario = new Usuario();
                                usuario.setNome(editCadNome.getText().toString());
                                usuario.setEmail(editCadEmail.getText().toString());
                                usuario.setSenha(editCadSenha.getText().toString());

                                cadastrarUsuario();
                            }else{
                                Toast.makeText(CadastroUser.this , "A senha da confirmação precisa ser igual a senha escolhida!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(CadastroUser.this, "E-mail inválido", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroUser.this, Login.class);
                startActivity(intent);
                CadastroUser.this.finish();
            }
        });
    }

    public static boolean validarEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUser.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUser.this);
                    preferencias.SalvarUsuarioPreferencias(identificadorUsuario, usuario.getNome());

                    abrirLoginUsuario();
                }else{
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail informado é inválido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Este e-mail já está cadastro no sistema.";
                    }catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUser.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUser.this, Login.class);
        startActivity(intent);
        finish();
    }
}
