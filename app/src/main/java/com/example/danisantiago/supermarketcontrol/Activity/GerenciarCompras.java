package com.example.danisantiago.supermarketcontrol.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.danisantiago.supermarketcontrol.Adapter.ComprasListAdapter;
import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.Helper.Base64Custom;
import com.example.danisantiago.supermarketcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GerenciarCompras extends AppCompatActivity {

    private RadioGroup listaCompras;
    private Button btnAddValor, btnExcluirCompra;
    private ArrayList<Compras> compras = new ArrayList<>();

    private FirebaseAuth usuarioFirebase;
    private DatabaseReference firebaseCompras;
    private DatabaseReference firebaseUsuario;

    private RadioButton radioButton;
    private Compras selecionado = new Compras();

    private String idUser = "";
    private Menu contaNome;
    String nomeUser = "";
    private AlertDialog alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_compras);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnAddValor = (Button) findViewById(R.id.btnAddValor);
        btnExcluirCompra = (Button) findViewById(R.id.btnExcluirCompra);

        FirebaseUser user = usuarioFirebase.getCurrentUser();
        idUser = Base64Custom.codificarBase64(user.getEmail());

        firebaseUsuario = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("nome");
        firebaseUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeUser = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseCompras = ConfiguracaoFirebase.getFirebase().child("usuario").child(idUser).child("listaCompras");
        firebaseCompras.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Compras> compras1 = new ArrayList<>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Compras novo = dados.getValue(Compras.class);
                    compras1.add(novo);

                }

                compras = compras1;
                listaCompras = (RadioGroup) findViewById(R.id.RadioListaCompras);
                for (Compras c : compras1) {
                    RadioButton btn = new RadioButton(GerenciarCompras.this);
                    btn.setId(Integer.valueOf(c.getId()));
                    btn.setText(c.getDiaCompra() + " Valor: R$" + String.valueOf(c.getValor()));
                    listaCompras.addView(btn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarCompras.this, EditarListaCompras.class);
                Bundle params = new Bundle();

                for (Compras c : compras) {
                    if (c.isCheck()) {
                        selecionado = c;
                        break;
                    }
                }

                intent.putExtra("compra", selecionado);
                startActivity(intent);
                finish();
            }
        });

        btnExcluirCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemSelecionado();
                if (selecionado == null) {
                    Toast.makeText(GerenciarCompras.this, "Selecione um item", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarCompras.this);
                    builder.setTitle("Atenção!");
                    builder.setMessage("Tem certeza que deseja apagar esta compra?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LimparCompra(selecionado);
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

            }
        });

        RadioGroup grupo = (RadioGroup) findViewById(R.id.RadioListaCompras);
        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton id = (RadioButton)findViewById(checkedId);
                SoSelecionado(id.getId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gerenciar_compras, menu);
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
        } else if (id == R.id.lixoListaCompra) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarCompras.this);
            builder.setTitle("Atenção!");
            builder.setMessage("Tem certeza que deseja apagar todas as compras?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ApagarListaCompras();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alerta = builder.create();
            alerta.show();
        } else if (id == R.id.visuCompras) {
            VisualizarCompras();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(GerenciarCompras.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void LimparCompra(Compras check) {
        firebaseCompras = ConfiguracaoFirebase.getFirebase();
        firebaseCompras.child("usuario").child(idUser).child("listaCompras").child(check.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null)
                    Toast.makeText(GerenciarCompras.this, "Erro ao apagar este item. Tente novamente ou entre em contato com o admnistrador!", Toast.LENGTH_LONG).show();
            }
        });
        AtualizarPagina();
    }

    private void ItemSelecionado() {
        for (Compras c : compras) {
            if (c.isCheck()) {
                selecionado = c;
                break;
            }
        }
    }

    private void IrHome() {
        Intent intent = new Intent(GerenciarCompras.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void ApagarListaCompras() {
        firebaseCompras = ConfiguracaoFirebase.getFirebase();
        firebaseCompras.child("usuario").child(idUser).child("listaCompras").removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(GerenciarCompras.this, "Erro ao apagar este item. Tente novamente ou entre em contato com o admnistrador!", Toast.LENGTH_LONG).show();
                }
            }
        });
        IrHome();
    }

    private void VisualizarCompras() {
        Intent intent = new Intent(GerenciarCompras.this, ListaCompras.class);
        startActivity(intent);
        finish();
    }

    private void SoSelecionado(int id){
        for(Compras c: compras){
            if(!c.getId().equals(String.valueOf(id))  && c.isCheck()){
                c.setCheck(false);
            }else if(c.getId().equals(String.valueOf(id))){
                c.setCheck(true);
            }
        }
    }

    private void AtualizarPagina(){
        Intent intent = new Intent(GerenciarCompras.this, GerenciarCompras.class);
        startActivity(intent);
        finish();
    }

}
