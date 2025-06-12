package com.pucpr.anotaai.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Lista implements Serializable {
    private int id;
    private String nomeResp;      // nomeResp da pessoa/responsável
    private String nomeLista;     // novo campo
    private String categoria;     // novo campo
    private LocalDate dataCriacao;   // novo campo

    public Lista(int id, String nomeLista, String categoria, String nomeResp, LocalDate dataCriacao) {
        this.id = id;
        this.nomeLista = nomeLista;
        this.nomeResp = nomeResp;
        this.categoria = categoria;
        this.dataCriacao = dataCriacao;
    }
    public int getId() {
        return id;
    }

    public String getNome() {
        return nomeResp;
    }

    public void setNome(String nomeResp) {
        this.nomeResp = nomeResp;
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
