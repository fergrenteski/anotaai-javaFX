package com.pucpr.anotaai.repository;

import com.pucpr.anotaai.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final File arquivo = new File("upload/users.txt");

    public UserRepository() {
        File dir = new File("upload");
        if (!dir.exists()) dir.mkdirs();
    }

    public List<User> carregar() {
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvar(List<User>users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
