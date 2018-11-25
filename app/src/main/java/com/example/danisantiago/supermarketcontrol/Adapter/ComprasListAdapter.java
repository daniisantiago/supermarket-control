package com.example.danisantiago.supermarketcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.R;

import java.util.ArrayList;

public class ComprasListAdapter extends ArrayAdapter<Compras> {
    private ArrayList<Compras> compras;
    private Context context;
    private RadioGroup grupo;

    public ComprasListAdapter(@NonNull Context context, ArrayList<Compras> compras) {
        super(context, 0, compras);

        this.context = context;
        this.compras = compras;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(compras != null){
            final Compras compras1 = compras.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.action_lista_compras, parent, false);

            final RadioButton radio = (RadioButton)view.findViewById(R.id.radioCompra);

            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radio.isChecked()){
                        compras1.setCheck(true);
                    }else{
                        compras1.setCheck(false);
                    }
                }
            });

            final String valor = String.valueOf(compras1.getValor());
            radio.setText(compras1.getDiaCompra() + " Valor: R$" + valor);
            grupo.addView(radio);


        }

        return view;
    }
}
