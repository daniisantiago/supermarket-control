package com.example.danisantiago.supermarketcontrol.Entidades;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Compras implements Serializable {

    private String id;
    private ArrayList<Produtos> listaProdutos;
    private String diaCompra;
    private double valor;
    private boolean isCheck;

    public Compras() {
        this.valor = 0.00;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
