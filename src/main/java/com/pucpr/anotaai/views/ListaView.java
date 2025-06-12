package com.pucpr.anotaai.views;

import com.pucpr.anotaai.service.ListService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListaView {
    private final ListService listService = ListService.getInstance();

    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("Tela de Listas");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Campo para adicionar lista
        TextField novaListaField = new TextField();
        novaListaField.setPromptText("Nome da nova lista");

        Button adicionarBtn = new Button("Adicionar");
        adicionarBtn.setOnAction(e -> {
            String nome = novaListaField.getText();
            if (!nome.trim().isEmpty()) {
                listService.adicionarLista(nome);
                novaListaField.clear();
            }
        });
        HBox addBox = new HBox(10, novaListaField, adicionarBtn);

        // ListView para mostrar as listas
        ListView<String> listView = new ListView<>(listService.getListas());

        // Botão para remover
        Button removerBtn = new Button("Remover Selecionado");
        removerBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listService.removerLista(selected);
            }
        });

        // Botão para editar
        Button editarBtn = new Button("Editar Selecionado");
        editarBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TextInputDialog dialog = new TextInputDialog(selected);
                dialog.setTitle("Editar Lista");
                dialog.setHeaderText("Editar nome da lista");
                dialog.setContentText("Novo nome:");

                dialog.showAndWait().ifPresent(novoNome -> {
                    if (!novoNome.trim().isEmpty()) {
                        listService.editarLista(selected, novoNome);
                    }
                });
            }
        });
        HBox buttonBox = new HBox(10, removerBtn, editarBtn);

        layout.getChildren().addAll(title, addBox, listView, buttonBox);

        stage.setScene(new Scene(layout, 400, 300));
        stage.setTitle("Listas");
        stage.show();
    }
}
