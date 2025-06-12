package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Lista;
import com.pucpr.anotaai.repository.ListaRepository;

import java.util.ArrayList;
import java.util.List;

public class ListaService {
    private final ListaRepository repository;
    private final List<Lista> locais;

    public ListaService(ListaRepository repository) {
        this.repository = repository;
        this.locais = new ArrayList<>(repository.carregar());
    }

    public List<Lista> listar() {
        return locais;
    }

    public void adicionar(Lista local) {
        locais.add(local);
        repository.salvar(locais);
    }

    public void atualizar(Lista atualizado) {
        for (int i = 0; i < locais.size(); i++) {
            if (locais.get(i).getId() == atualizado.getId()) {
                locais.set(i, atualizado);
                break;
            }
        }
        repository.salvar(locais);
    }

    public void remover(Lista local) {
        locais.remove(local);
        repository.salvar(locais);
    }

    public int gerarNovoId() {
        return locais.stream().mapToInt(Lista::getId).max().orElse(0) + 1;
    }
}
