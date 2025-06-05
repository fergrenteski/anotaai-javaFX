package com.pucpr.anotaai.navigation;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuFactory {

    private final NavegadorImpl navegador;

    public MenuFactory(NavegadorImpl navegadorImpl) {
        this.navegador = navegadorImpl;
    }

    public MenuBar criarMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem usuarioItem = new MenuItem("Usuário");
        usuarioItem.setOnAction(_ -> navegador.abrirUsuario());

        MenuItem grupoItem = new MenuItem("Grupos");
        grupoItem.setOnAction(_ -> navegador.abrirGrupo());

        MenuItem listaItem = new MenuItem("Listas");
        listaItem.setOnAction(_ -> navegador.abrirLista());

        MenuItem produtoItem = new MenuItem("Produtos");
        produtoItem.setOnAction(_ -> navegador.abrirProduto());

        MenuItem localItem = new MenuItem("Locais");
        localItem.setOnAction(_ -> navegador.abrirLocal());

        menu.getItems().addAll(usuarioItem, grupoItem, listaItem, produtoItem, localItem);
        menuBar.getMenus().add(menu);

        return menuBar;
    }
}