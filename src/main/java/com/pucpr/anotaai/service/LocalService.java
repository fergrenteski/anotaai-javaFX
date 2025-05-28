package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Local;
import com.pucpr.anotaai.repository.LocalRepository;

import java.util.ArrayList;
import java.util.List;

public class LocalService {
    private final LocalRepository repository;
    private final List<Local> locais;

    public LocalService(LocalRepository repository) {
        this.repository = repository;
        this.locais = new ArrayList<>(repository.carregar());
    }

    public List<Local> listar() {
        return locais;
    }

    public void adicionar(Local local) {
        locais.add(local);
        repository.salvar(locais);
    }

    public void atualizar(Local atualizado) {
        for (int i = 0; i < locais.size(); i++) {
            if (locais.get(i).getId() == atualizado.getId()) {
                locais.set(i, atualizado);
                break;
            }
        }
        repository.salvar(locais);
    }

    public void remover(Local local) {
        locais.remove(local);
        repository.salvar(locais);
    }

    public int gerarNovoId() {
        return locais.stream().mapToInt(Local::getId).max().orElse(0) + 1;
    }
}
