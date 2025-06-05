package com.pucpr.anotaai.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListService {

    private static ListService instance;
    private ObservableList<String> items;

    private ListService() {
        this.items = FXCollections.observableArrayList();
    }

    public static ListService getInstance() {
        if (instance == null) {
            instance = new ListService();
        }
        return instance;
    }

    public ObservableList<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        if (item != null && !item.trim().isEmpty()) {
            items.add(item);
        }
    }

    public boolean removeItem(String item) {
        return items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public int getSize() {
        return items.size();
    }

    public void removerLista(String nome) {
        instance.removerLista(nome);
    }
    public ObservableList<String> getListas() {
        return items;
    }

    public void adicionarLista(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
        }  items.add(nome);
        }
    }