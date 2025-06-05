package com.pucpr.anotaai.model;


import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String nome;
    private String biografia;
    private String cpf;
    private String email;
    private String dataNascimento;

    public User() {}

    public User(int id, String nome, String biografia, String cpf, String email, String dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.biografia = biografia;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public int getId() {
        return id;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
}
