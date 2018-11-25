package com.example.danisantiago.supermarketcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.R;

import java.util.ArrayList;

public class ProdutosAdapter extends ArrayAdapter<Produtos> {
    private ArrayList<Produtos> produtos;
    private Context context;

    public ProdutosAdapter(@NonNull Context c, ArrayList<Produtos> objetos) {
        super(c, 0, objetos);

        this.context = c;
        this.produtos = objetos;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(produtos != null){
            final Produtos produtos1 = produtos.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drop_down_supermercados, parent, false);

            final CheckBox txtNome = (CheckBox) view.findViewById(R.id.dropNome);

            txtNome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtNome.isChecked()){
                       produtos1.setEscolhido(true);
                   }else{
                       produtos1.setEscolhido(false);
                   }
                }
            });

            txtNome.setText(produtos1.getNome());
        }

        return view;
    }
}
