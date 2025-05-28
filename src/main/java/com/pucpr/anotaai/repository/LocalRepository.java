package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.Local;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalRepository {
    private final File arquivo = new File("upload/locais.txt");

    public LocalRepository() {
        File dir = new File("upload");
        if (!dir.exists()) dir.mkdirs();
    }

    public List<Local> carregar() {
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Local>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(List<Local> locais) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(locais);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
