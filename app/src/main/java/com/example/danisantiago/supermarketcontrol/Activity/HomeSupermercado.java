package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.danisantiago.supermarketcontrol.Adapter.SupermercadosAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.Entidades.Supermercados;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeSupermercado extends AppCompatActivity {
    private Spinner dropDown;

    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebase;

    private Button btnCriarLista, btnCadastroSupermercado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firebase = ConfiguracaoFirebase.getFirebase().child("supermercados");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Supermercados> mercados = new ArrayList<>();
                ArrayList<String>nomesMercado = new ArrayList<>();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Supermercados novo = dados.getValue(Supermercados.class);
                    mercados.add(novo);
                    String nomes = dados.child("nome").getValue(String.class);
                    nomesMercado.add(nomes);
                }

                dropDown = (Spinner)findViewById(R.id.dropDown);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeSupermercado.this, android.R.layout.simple_spinner_item, nomesMercado);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropDown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });


           }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.sair){
            deslogarUsuario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(HomeSupermercado.this, Login.class);
        startActivity(intent);
        finish();
    }
}
