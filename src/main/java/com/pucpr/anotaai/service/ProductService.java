package com.pucpr.anotaai.service;

import com.pucpr.anotaai.model.Produto;
import com.pucpr.anotaai.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final ProductRepository repository;
    private final List<Produto> produtos;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        // Carrega tudo que existe em disco, para trabalhar em memória
        this.produtos = new ArrayList<>(repository.carregar());
    }

    /**
     * Retorna uma cópia da lista de produtos (para que a lista interna não seja alterada fora do Service).
     */
    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    /**
     * Adiciona um novo produto.
     * Atenção: o objeto Produto que entrar aqui
     * já deve ter o ID definido (pode chamar gerarNovoId() antes de montar o objeto).
     */
    public void adicionar(Produto produto) {
        produtos.add(produto);
        repository.salvar(produtos);
    }

    /**
     * Atualiza um produto existente (identificado pelo mesmo ID).
     * Substitui o objeto na lista em memória e salva tudo em disco.
     */
    public void atualizar(Produto atualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == atualizado.getId()) {
                produtos.set(i, atualizado);
                break;
            }
        }
        repository.salvar(produtos);
    }

    /**
     * Remove o produto (objeto igual ou que possua o mesmo ID) da lista em memória e persiste em disco.
     */
    public void remover(Produto produto) {
        produtos.remove(produto);
        repository.salvar(produtos);
    }

    /**
     * Gera um novo ID, baseado no maior ID atual + 1.
     * Se não houver nenhum produto, retorna 1.
     */
    public int gerarNovoId() {
        return produtos.stream()
                .mapToInt(Produto::getId)
                .max()
                .orElse(0) + 1;
    }
}
