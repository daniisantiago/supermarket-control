package com.example.danisantiago.supermarketcontrol.Entidades;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;

public class Compras {

    private String id;
    private ArrayList<Produtos> listaProdutos;
    private String diaCompra;

    public Compras() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Produtos> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(ArrayList<Produtos> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public String getDiaCompra() {
        return diaCompra;
    }

    public void setDiaCompra(String diaCompra) {
        this.diaCompra = diaCompra;
    }

}
