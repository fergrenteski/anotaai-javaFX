package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.List;

import java.io.*;
import java.util.ArrayList;

public class ListRepository {
    private final File arquivo = new File("upload/listas.txt");

    public ListRepository() {
        File dir = new File("upload");
        if (!dir.exists()) dir.mkdirs();
    }

    @SuppressWarnings("unchecked")
    public java.util.List<List> carregar() {
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (java.util.List<List>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public void salvar(java.util.List<List> listas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(listas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
