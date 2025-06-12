package com.pucpr.anotaai.model;

import java.io.Serializable;

public class List implements Serializable {
    private int id;
    private String nome;          // nome da pessoa/responsável
    private String nomeLista;     // novo campo
    private String categoria;     // novo campo
    private String dataCriacao;   // novo campo

    public List(int id, String nome, String nomeLista, String categoria, String dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.nomeLista = nomeLista;
        this.categoria = categoria;
        this.dataCriacao = dataCriacao;
    }
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
