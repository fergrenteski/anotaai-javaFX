package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Grupo;
import com.pucpr.anotaai.repository.GrupoRepository;

import java.util.List;

public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> listar() {
        return grupoRepository.carregar();
    }

    public void adicionar(Grupo grupo) {
        List<Grupo> grupos = listar();
        grupos.add(grupo);
        grupoRepository.salvar(grupos);
    }

    public void atualizar(Grupo grupo) {
        List<Grupo> grupos = listar();
        for (int i = 0; i < grupos.size(); i++) {
            if (grupos.get(i).getId() == grupo.getId()) {
                grupos.set(i, grupo);
                break;
            }
        }
        grupoRepository.salvar(grupos);
    }

    public void remover(Grupo grupo) {
        List<Grupo> grupos = listar();
        grupos.removeIf(g -> g.getId() == grupo.getId());
        grupoRepository.salvar(grupos);
    }

    public int gerarNovoId() {
        List<Grupo> grupos = listar();
        return grupos.stream().mapToInt(Grupo::getId).max().orElse(0) + 1;
    }
}
