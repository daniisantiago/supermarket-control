package com.example.danisantiago.supermarketcontrol.Entidades;

import com.example.danisantiago.supermarketcontrol.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nome;
    private String senha;
    private String email;
    private ArrayList<Produtos> listaProdutos;
    private ArrayList<Compras> listaCompras;

    public Usuario(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Produtos> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(ArrayList<Produtos> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public ArrayList<Compras> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(ArrayList<Compras> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public void salvar(){
        DatabaseReference referenceFirebase = ConfiguracaoFirebase.getFirebase();
        referenceFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("listaProdutos", getListaProdutos());
        hashMapUsuario.put("listaCompras", getListaCompras());

        return hashMapUsuario;
    }


}
