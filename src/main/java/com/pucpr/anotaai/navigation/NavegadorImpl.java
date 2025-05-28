package com.pucpr.anotaai.navigation;

import com.pucpr.anotaai.views.*;
import javafx.stage.Stage;

public class NavegadorImpl implements Navegador {
    public void abrirUsuario() {
        new UsuarioView().start(new Stage());
    }

    public void abrirGrupo() {
        new GrupoView().start(new Stage());
    }

    public void abrirLista() {
        new ListaView().start(new Stage());
    }

    public void abrirProduto() {
        new ProdutoView().start(new Stage());
    }

    public void abrirLocal() {
        new LocalView().start(new Stage());
    }
}
