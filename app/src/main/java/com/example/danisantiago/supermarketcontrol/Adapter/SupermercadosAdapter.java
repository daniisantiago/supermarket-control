package com.example.danisantiago.supermarketcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.danisantiago.supermarketcontrol.Entidades.Supermercados;
import com.example.danisantiago.supermarketcontrol.R;

import java.util.ArrayList;

public class SupermercadosAdapter extends ArrayAdapter<Supermercados> {

    private ArrayList<Supermercados> supermercados;
    private Context context;
    int layoutId;

    public SupermercadosAdapter(Context c, int layout, ArrayList<Supermercados> objetos){
        super(c, layout, objetos);

        this.context = c;
        this.supermercados = objetos;
        this.layoutId = layout;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Supermercados getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Supermercados item = getItem(position);
        String nome = item.getNome();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, parent, false);
        }

        TextView nomeView = (TextView) convertView.findViewById(R.id.dropNome);

        nomeView.setText(nome);
        return convertView;
    }
}
