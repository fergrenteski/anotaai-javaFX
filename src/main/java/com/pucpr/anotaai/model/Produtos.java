package com.pucpr.anotaai.model;

public class Produtos {

    private int id;
    private String nome;
    private String descricao;
    private String categoria;
    private double preco;
    private int quantidade;

    //enum

    public Produtos(int id,String nome, String descricao, String categoria, double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public int getId() {return id;}
    // Nome
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    // Descrição
    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    // Categoria
    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}

    // Preço
    public double getPreco() {return preco;}
    public void setPreco(double preco) {this.preco = preco;}

    // Quantidade
    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

}


