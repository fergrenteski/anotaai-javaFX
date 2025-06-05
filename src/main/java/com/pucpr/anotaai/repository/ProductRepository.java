package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.Produtos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final File arquivo = new File("upload/produtos.txt");

    public ProductRepository() {
        File dir = new File("upload");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public List<Produtos> carregar() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Produtos>) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(List<Produtos> produtos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
