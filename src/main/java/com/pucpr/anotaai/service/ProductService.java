package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Produtos;
import com.pucpr.anotaai.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final ProductRepository repository;
    private final List<Produtos> produtos;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.produtos = new ArrayList<>(repository.carregarProdutos());
    }

    public List<Produtos> listar() {
        return produtos;
    }

    public void adicionar(Produtos produto) {
        produtos.add(produto);
        repository.salvar(produtos);
    }

    public void atualizar(Produtos atualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == atualizado.getId()) {
                produtos.set(i, atualizado);
                break;
            }
        }
        repository.salvar(produtos);
    }

    public void remover(Produtos produto) {
        produtos.remove(produto);
        repository.salvar(produtos);
    }

    public int gerarNovoId() {
        return produtos.stream()
                .mapToInt(Produtos::getId)
                .max()
                .orElse(0) + 1;
    }
}
