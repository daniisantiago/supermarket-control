package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.Adapter.ComprasAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaCompras extends AppCompatActivity {
    private FirebaseAuth usuario;
    private DatabaseReference firebase;
    private DatabaseReference fireUsuario;

    private String idUser = "";
    private Menu contaNome;
    private ListView listarCompras;
    String nomeUser = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visu_compras);

        usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();


        FirebaseUser user = usuario.getCurrentUser();
        idUser = Base64Custom.codificarBase64(user.getEmail());

        fireUsuario = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("nome");
        fireUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeUser = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (idUser != null) {
            firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaCompras");
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final ArrayList<Compras> compras = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Compras compra = dados.getValue(Compras.class);
                            compras.add(compra);
                        }

                        listarCompras = (ListView) findViewById(R.id.listarCompas);
                        final ComprasAdapter adapter = new ComprasAdapter(ListaCompras.this, compras);
                        listarCompras.setAdapter(adapter);
                    } else {
                        Toast.makeText(ListaCompras.this, "Nenhuma compra realizada ainda!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_compras, menu);
        contaNome = menu;
        contaNome.findItem(R.id.pessoa).setTitle(nomeUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sair) {
            deslogarUsuario();
        } else if (id == R.id.visuHome) {
            IrHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuario.signOut();
        Intent intent = new Intent(ListaCompras.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void IrHome() {
        Intent intent = new Intent(ListaCompras.this, Home.class);
        startActivity(intent);
        finish();
    }
}
