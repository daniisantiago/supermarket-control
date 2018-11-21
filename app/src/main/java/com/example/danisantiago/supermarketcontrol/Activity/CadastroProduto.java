package com.example.danisantiago.supermarketcontrol.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class CadastroProduto extends AppCompatActivity {

    private Produtos produto;
    private EditText editProduto;
    private Button btnAdicionar, btnVoltar;
    private ArrayList<Produtos> lista;

    private FirebaseAuth usuarioFirebase;

    private DatabaseReference firebase;
    private DatabaseReference fireProduto;
    private String idUser = "";
    private ArrayList<Produtos> temProduto = new ArrayList<>();
    private boolean achou = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        editProduto = (EditText) findViewById(R.id.editProduto);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUser = Base64Custom.codificarBase64(usuarioFirebase.getCurrentUser().getEmail());

        btnAdicionar = (Button) findViewById(R.id.btn_add_produto);
        btnVoltar = (Button) findViewById(R.id.btn_voltar_home);


        fireProduto = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaProdutos");
        fireProduto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Produtos> aux = new ArrayList<Produtos>();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Produtos p = dados.getValue(Produtos.class);
                    aux.add(p);
                }
                temProduto = aux;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editProduto == null) {
                    Toast.makeText(CadastroProduto.this, "Informe o nome do produto!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent it = getIntent();
                    if (it != null) {
                        produto = new Produtos();
                        produto.setNome(editProduto.getText().toString());
                        String id = UUID.randomUUID().toString();
                        produto.setId(id);
                        produto.setEscolhido(false);
                        for (Produtos find : temProduto) {
                            if (find.getNome().equals(produto.getNome())) {
                                achou = true;
                                break;
                            }
                        }

                        Bundle params = it.getExtras();
                        if(params != null){
                            Integer valor = params.getInt("contexto");
                            if (achou == false) {
                                salvarProduto(produto, usuarioFirebase);
                                if (valor == 1) {
                                    voltarHome();
                                } else {
                                    lista = (ArrayList<Produtos>) it.getSerializableExtra("produtos");
                                    lista.add(produto);
                                    voltarCompras(lista);
                                }
                            }else {
                                achou = false;
                                if(valor == 1){
                                    Toast.makeText(CadastroProduto.this, "Esse produto já está cadastrado!", Toast.LENGTH_LONG).show();
                                }else{
                                    lista = (ArrayList<Produtos>) it.getSerializableExtra("produtos");
                                    lista.add(produto);
                                    voltarCompras(lista);
                                }
                            }
                        }
                    }
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = getIntent();
                if (it != null) {
                    Bundle params = it.getExtras();
                    if (params != null) {
                        Integer valor = params.getInt("contexto");
                        if (valor == 1) {
                            voltarHome();
                        } else {
                            lista = (ArrayList<Produtos>) it.getSerializableExtra("produtos");
                            voltarCompras(lista);
                        }
                    }
                }
            }
        });
    }

    private boolean salvarProduto(Produtos produto, FirebaseAuth user) {
        try {
            String chave = String.valueOf(produto.getId());

            if (user.getCurrentUser() != null) {
                firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaProdutos");
                firebase.child(chave).setValue(produto);
                Toast.makeText(this, "Produto inserido com sucesso", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void voltarHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }


    private void voltarCompras(ArrayList<Produtos> novos) {
        Intent intent = new Intent(this, InicioCompra.class);
        intent.putExtra("produtos", novos);
        startActivity(intent);
        finish();
    }
}

