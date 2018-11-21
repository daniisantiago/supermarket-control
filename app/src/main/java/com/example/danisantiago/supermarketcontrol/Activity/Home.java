package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.Adapter.ProdutosAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.Entidades.Supermercados;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private ListView listShops;
    private Button btnModal, btnInicioCompra;
    private TextView vazia;
    private ArrayList<Produtos> proximo = new ArrayList<Produtos>();
    private ArrayList<Produtos> testList = new ArrayList<>();

    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebase;
    private DatabaseReference firebaseProdutos;
    private DatabaseReference fireUsuario;

    private CheckBox checkBox;
    private final ArrayList<Produtos> selecionados = new ArrayList<>();
    private String idUser = "";
    private Menu contaNome;
    String nomeUser = "";
    private AlertDialog alerta;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnModal = (Button) findViewById(R.id.btn_chamar_modal);

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

        if (idUser != null) {
            firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaProdutos");
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final ArrayList<Produtos> produtos = new ArrayList<>();

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Produtos novo = dados.getValue(Produtos.class);
                        produtos.add(novo);
                    }
                    listShops = (ListView) findViewById(R.id.ListaCompras);
                    final ProdutosAdapter adapter = new ProdutosAdapter(Home.this, produtos);
                    listShops.setAdapter(adapter);

                    proximo = produtos;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        btnModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, CadastroProduto.class);
                Bundle params = new Bundle();
                params.putInt("contexto", 1);
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });

        btnInicioCompra = (Button) findViewById(R.id.btn_iniciar_compra);
        btnInicioCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proximo != null) {
                    for (Produtos p : proximo) {
                        if (p.isEscolhido()) {
                            selecionados.add(p);
                        }
                    }
                }
                if (selecionados.isEmpty()) {
                    Toast.makeText(Home.this, "Selecione os produtos para iniciar as compras!", Toast.LENGTH_LONG).show();
                } else {
                    IniciarCompras();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        contaNome = menu;
        contaNome.findItem(R.id.pessoa).setTitle(nomeUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sair) {
            deslogarUsuario();
        }else if(id == R.id.visuCompras){
            VisualizarCompras();
        }else  if(id == R.id.lixoLista){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atenção!");
            builder.setMessage("Tem certeza que deseja apagar toda a lista de produtos?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LimparLista();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alerta = builder.create();
            alerta.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(Home.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void IniciarCompras() {
        Intent intent = new Intent(Home.this, InicioCompra.class);
        intent.putExtra("produtos", selecionados);
        startActivity(intent);
        finish();
    }

    private void VisualizarCompras(){
        Intent intent = new Intent(Home.this, ListaCompras.class);
        startActivity(intent);
        finish();
    }

    private void LimparLista(){
        firebaseProdutos = ConfiguracaoFirebase.getFirebase();
        firebaseProdutos.child("usuario").child(idUser).child("listaProdutos").removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError != null){
                    Toast.makeText(Home.this, "Erro ao apagar a lista de produtos! Tente mais tarde.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
