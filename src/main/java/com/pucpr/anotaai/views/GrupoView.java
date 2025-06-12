package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Grupo;
import com.pucpr.anotaai.repository.GrupoRepository;
import com.pucpr.anotaai.service.GrupoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import javafx.stage.Stage;

public class GrupoView {

    private final ObservableList<Grupo> grupos = FXCollections.observableArrayList();
    private final GrupoRepository grupoRepository = new GrupoRepository();
    private final GrupoService grupoService = new GrupoService(grupoRepository);

    public void start(Stage stage) {
        grupos.setAll(grupoService.listar());

        TableView<Grupo> tabela = new TableView<>(grupos);
        tabela.setPrefHeight(300);

        TableColumn<Grupo, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Grupo, String> descCol = new TableColumn<>("Descrição");
        descCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Grupo, String> catCol = new TableColumn<>("Categoria");
        catCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Editar
        TableColumn<Grupo, Void> editarCol = new TableColumn<>("Editar");
        editarCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("✏️");
            {
                btn.setOnAction(_ -> abrirFormulario(tabela.getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Excluir
        TableColumn<Grupo, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setOnAction(_ -> {
                    Grupo grupo = tabela.getItems().get(getIndex());
                    ModalConfirmacao.mostrar("Confirmação",
                        "Deseja excluir o grupo '" + grupo.getNome() + "'?",
                        () -> {
                            grupos.remove(grupo);
                            grupoService.remover(grupo);
                        });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tabela.getColumns().addAll(nomeCol, descCol, catCol, editarCol, excluirCol);

        Button novoBtn = new Button("Novo Grupo");
        novoBtn.setOnAction(_ -> abrirFormulario(null));

        VBox layout = new VBox(10, new Label("Tela de Grupos"), tabela, novoBtn);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Grupos");
        stage.show();
    }

    private void abrirFormulario(Grupo grupo) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(grupo == null ? "Novo Grupo" : "Editar Grupo");

        TextField nomeField = new TextField(grupo != null ? grupo.getNome() : "");
        TextField descField = new TextField(grupo != null ? grupo.getDescricao() : "");
        TextField catField = new TextField(grupo != null ? grupo.getCategoria() : "");

        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(_ -> {
            String nome = nomeField.getText();
            String mensagem = grupo == null
                    ? "Deseja salvar o grupo '" + nome + "'?"
                    : "Deseja atualizar o grupo '" + nome + "'?";

            if (nomeField.getText().isEmpty() || descField.getText().isEmpty() || catField.getText().isEmpty()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios");
            }

            ModalConfirmacao.mostrar("Confirmação", mensagem, () -> {
                if (grupo == null) {
                    Grupo novo = new Grupo(grupoService.gerarNovoId(), nomeField.getText(), descField.getText(), catField.getText());
                    grupos.add(novo);
                    grupoService.adicionar(novo);
                } else {
                    grupo.setNome(nomeField.getText());
                    grupo.setDescricao(descField.getText());
                    grupo.setCategoria(catField.getText());
                    int index = grupos.indexOf(grupo);
                    grupos.set(index, grupo);
                    grupoService.atualizar(grupo);
                }
                modal.close();
            });
        });

        VBox form = new VBox(10,
                new Label("Nome:*"), nomeField,
                new Label("Descrição:*"), descField,
                new Label("Categoria:*"), catField,
                salvarBtn
        );
        form.setPadding(new Insets(20));

        modal.setScene(new Scene(form, 300, 400));
        modal.showAndWait();
    }
}
