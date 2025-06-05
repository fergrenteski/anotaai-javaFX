package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Local;
import com.pucpr.anotaai.model.User;
import com.pucpr.anotaai.repository.LocalRepository;
import com.pucpr.anotaai.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserRepository repository;
    private final List<User> users;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.users = new ArrayList<>(repository.carregar());
    }

    public List<User> listar() {
        return users;
    }

    public void adicionar(User user) {
        users.add(user);
        repository.salvar(users);
    }

    public void atualizar(User atualizado) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == atualizado.getId()) {
                users.set(i, atualizado);
                break;
            }
        }
        repository.salvar(users);
    }

    public void remover(User user) {
        users.remove(user);
        repository.salvar(users);
    }

    public int gerarNovoId() {
        return users.stream().mapToInt(User::getId).max().orElse(0) + 1;
    }
}
