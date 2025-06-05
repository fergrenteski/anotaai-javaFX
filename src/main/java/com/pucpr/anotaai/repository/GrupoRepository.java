package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.Grupo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoRepository {
    private final File arquivo = new File("upload/grupos.txt");

    public GrupoRepository() {
        File dir = new File("upload");
        if (!dir.exists()) dir.mkdirs();
    }

    public List<Grupo> carregar() {
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Grupo>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(List<Grupo> grupos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(grupos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}