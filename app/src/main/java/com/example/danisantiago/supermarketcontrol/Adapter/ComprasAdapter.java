package com.example.danisantiago.supermarketcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danisantiago.supermarketcontrol.Entidades.Compras;
import com.example.danisantiago.supermarketcontrol.Entidades.Produtos;
import com.example.danisantiago.supermarketcontrol.R;

import java.util.ArrayList;

public class ComprasAdapter extends ArrayAdapter<Compras> {

    private ArrayList<Compras> compras;
    private Context context;

    private ListView listView;

    public ComprasAdapter(@NonNull Context context, ArrayList<Compras> objetos) {
        super(context, 0, objetos);
        this.compras = objetos;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        final ViewHolder viewHolder = new ViewHolder();
        if (compras != null) {
            final Compras compras1 = compras.get(position);
            final String valor;

            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.activity_compras, parent, false);

            viewHolder.txtDia = (TextView) view.findViewById(R.id.diaCompra);
            valor  = String.valueOf(compras1.getValor());
            viewHolder.txtDia.setText(compras1.getDiaCompra() + " Valor: R$" + valor);
            viewHolder.listView = (ListView) view.findViewById(R.id.listCompra);

            viewHolder.imgButton = (ImageButton) view.findViewById(R.id.ImgCompra);

            viewHolder.imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewHolder.listView.getVisibility() == View.VISIBLE) {
                        viewHolder.listView.setVisibility(View.GONE);
                    } else {
                        ProdutosListAdapter adapter = new ProdutosListAdapter(context, compras1.getListaProdutos());
                        viewHolder.listView.setAdapter(adapter);
                        viewHolder.listView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        return view;
    }

    private static class ViewHolder {
        TextView txtDia;
        ListView listView;
        ImageButton imgButton;
    }
}
