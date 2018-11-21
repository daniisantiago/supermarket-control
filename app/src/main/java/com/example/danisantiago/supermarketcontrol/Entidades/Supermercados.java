package com.example.danisantiago.supermarketcontrol.Entidades;

import java.util.List;

public class Supermercados {
    private int id;
    private String nome;
    private List<Produtos> produtosList;

    public Supermercados() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Produtos> getProdutosList() {
        return produtosList;
    }

    public void setProdutosList(List<Produtos> produtosList) {
        this.produtosList = produtosList;
    }
}
