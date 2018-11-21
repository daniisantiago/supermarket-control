package com.example.danisantiago.supermarketcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.R;

import java.util.ArrayList;

public class ProdutosListAdapter extends ArrayAdapter<Produtos>{
    private ArrayList<Produtos> produtos;
    private Context context;

    public ProdutosListAdapter(@NonNull Context c, ArrayList<Produtos> objetos) {
        super(c, 0, objetos);

        this.context = c;
        this.produtos = objetos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(produtos != null){
            final Produtos produtos1 = produtos.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_lista_produtos, parent, false);

            TextView txtNome = (TextView) view.findViewById(R.id.listProd);
            txtNome.setText(produtos1.getNome());
        }

        return view;
    }
}
