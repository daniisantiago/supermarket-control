package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.danisantiago.supermarketcontrol.Adapter.ProdutosAdapter;
import com.example.danisantiago.supermarketcontrol.Adapter.ProdutosListAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EditarListaCompras extends AppCompatActivity {

    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebase;
    private DatabaseReference fireUsuario;


    private ListView listaProdutos;
    private EditText valor;
    private Button confirmar;
    private Compras compra;
    private ArrayList<Produtos> produtos;

    private String idUser = "";
    private Menu contaNome;
    String nomeUser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lista_compras);

        Intent intent = getIntent();

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        FirebaseUser user = usuarioFirebase.getCurrentUser();
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

        if(intent != null){
            compra = (Compras)intent.getSerializableExtra("compra");
            produtos = compra.getListaProdutos();

            listaProdutos = (ListView)findViewById(R.id.editListCompras);
            ProdutosListAdapter adapter = new ProdutosListAdapter(EditarListaCompras.this, produtos);
            listaProdutos.setAdapter(adapter);
        }

        valor = (EditText)findViewById(R.id.editValor);
        valor.setText(String.valueOf(compra.getValor()));

        confirmar = (Button)findViewById(R.id.btnConfirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase = ConfiguracaoFirebase.getFirebase();
                firebase.child("usuario").child(idUser).child("listaCompras").child(compra.getId())
                        .child("valor").setValue(Double.valueOf(valor.getText().toString()));

                IrGenrenciarCompras();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar_compras, menu);
        contaNome = menu;
        contaNome.findItem(R.id.pessoa).setTitle(nomeUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sair) {
            deslogarUsuario();
        } else if (id == R.id.gerencial) {
            IrGenrenciarCompras();
        } else if (id == R.id.visuHome) {
            voltarHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(EditarListaCompras.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void voltarHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    private void IrGenrenciarCompras(){
        Intent intent = new Intent(EditarListaCompras.this, GerenciarCompras.class);
        startActivity(intent);
        finish();
    }
}
