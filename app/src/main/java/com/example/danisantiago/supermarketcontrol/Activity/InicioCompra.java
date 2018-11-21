package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.Adapter.ProdutosAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.Entidades.Usuario;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class InicioCompra extends AppCompatActivity {


    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebase;
    private DatabaseReference fireUsuario;
    private ListView listInicio;
    private ArrayList<Produtos> lista;
    private AlertDialog alerta;
    private Button btnFinalizar;
    private Compras compras;
    private String diaAtual = DateFormat.getDateInstance().format(new Date());
    private Menu contaNome;
    private int ultimoId;
    private ArrayList<Compras> testarMetodo;
    String idUser ="";
    String nomeUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_compra);

        Intent intent = getIntent();

        listInicio = (ListView) findViewById(R.id.listInicio);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUser = Base64Custom.codificarBase64(usuarioFirebase.getCurrentUser().getEmail());

        firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaCompras");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Compras> auxCompras = new ArrayList<>();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Compras nova = dados.getValue(Compras.class);
                    auxCompras.add(nova);
                }
                testarMetodo = new ArrayList<>();
                testarMetodo = auxCompras;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        

        if (intent != null) {
            lista = (ArrayList<Produtos>) intent.getSerializableExtra("produtos");
            for (Produtos p : lista) {
                p.setEscolhido(false);
            }
            ProdutosAdapter adapter = new ProdutosAdapter(InicioCompra.this, lista);
            listInicio.setAdapter(adapter);
        }

        btnFinalizar = (Button) findViewById(R.id.btnFimCompra);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalizarCompra(lista);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compra, menu);
        contaNome = menu;
        contaNome.findItem(R.id.pessoa).setTitle(nomeUser);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sair) {
            deslogarUsuario();
        } else if (id == R.id.maisProduto) {
            AdicionarProduto();
        } else if (id == R.id.visuHome) {
            voltarHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(InicioCompra.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void AdicionarProduto() {
        Intent intent = new Intent(InicioCompra.this, CadastroProduto.class);
        intent.putExtra("produtos", lista);
        startActivity(intent);
    }

    private void FinalizarCompra(ArrayList<Produtos> prods) {
        final ArrayList<Produtos> naoSelecionado = new ArrayList<Produtos>();
        final ArrayList<Produtos> selecionado = new ArrayList<Produtos>();
        compras = new Compras();
        if (prods != null) {
            for (Produtos a : prods) {
                if (a.isEscolhido() == false) {
                    naoSelecionado.add(a);
                } else {
                    selecionado.add(a);
                }
            }

            if (naoSelecionado.isEmpty()) {
                compras.setListaProdutos(prods);

                compras.setDiaCompra(diaAtual);
                salvarCompras(compras, firebase);
                Toast.makeText(this, "Compra Finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                voltarHome();
            } else {
                int count = 1;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Atenção!");
                String message = "Faltou comprar os seguintes itens: ";
                for (Produtos a : naoSelecionado) {
                    message += a.getNome();
                    if (count < naoSelecionado.size()) {
                        message += ", ";
                        count++;
                    }
                }
                builder.setMessage(message);
                builder.setPositiveButton("Finalizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        compras.setListaProdutos(selecionado);
                        compras.setDiaCompra(diaAtual);
                        salvarCompras(compras,firebase);
                        Toast.makeText(InicioCompra.this, "Compra Finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                        voltarHome();
                    }
                });

                builder.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alerta = builder.create();
                alerta.show();
            }
        }
    }

    private void voltarHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    private boolean salvarCompras(Compras compra , DatabaseReference database) {
        try {
//            String chave = String.valueOf(compra.getId());

            for(Compras aux: testarMetodo){
                ultimoId = Integer.valueOf(aux.getId()) + 1;
            }

            compras.setId(String.valueOf(ultimoId));
            String chave = String.valueOf(ultimoId);
            firebase.child(chave).setValue(compra);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
