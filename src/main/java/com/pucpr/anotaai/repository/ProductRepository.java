package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // Arquivo onde a lista de produtos será serializada
    private final File arquivo = new File("upload/produtos.txt");

    public ProductRepository() {
        // Garante que a pasta "upload" exista
        File dir = new File("upload");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Produto> carregar() {
        if (!arquivo.exists()) {
            // Se não existir ainda, retorna lista vazia
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Produto>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(List<Produto> produtos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
