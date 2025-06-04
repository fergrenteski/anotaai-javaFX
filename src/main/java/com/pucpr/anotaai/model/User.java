package com.pucpr.anotaai.model;

import java.time.LocalDate;

public class User {

    private String nome;
    private String biografia;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;

    public User() {}

    public User(String nome, String biografia, String cpf, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.biografia = biografia;
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}
