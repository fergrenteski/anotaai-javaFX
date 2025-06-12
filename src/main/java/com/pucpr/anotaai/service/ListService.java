package com.pucpr.anotaai.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListService {

    private static ListService instance;
    private ObservableList<String> listas;

    private ListService() {
        this.listas = FXCollections.observableArrayList();
    }

    public static ListService getInstance() {
        if (instance == null) {
            instance = new ListService();
        }
        return instance;
    }

    public ObservableList<String> getListas() {
        return listas;
    }

    public void adicionarLista(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            listas.add(nome);
        }
    }

    public boolean removerLista(String nome) {
        return listas.remove(nome);
    }

    public boolean editarLista(String nomeAntigo, String nomeNovo) {
        if (nomeAntigo == null || nomeNovo == null || nomeNovo.trim().isEmpty()) {
            return false;
        }
        int index = listas.indexOf(nomeAntigo);
        if (index >= 0) {
            listas.set(index, nomeNovo);
            return true;
        }
        return false;
    }
    public void limparListas() {
        listas.clear();
    }

    public int getQuantidadeListas() {
        return listas.size();
    }
}
