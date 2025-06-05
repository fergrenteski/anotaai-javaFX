package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Local;
import com.pucpr.anotaai.repository.LocalRepository;
import com.pucpr.anotaai.service.LocalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LocalView {

    private final ObservableList<Local> locais = FXCollections.observableArrayList();
    private final LocalRepository localRepository = new LocalRepository();
    private final LocalService localService = new LocalService(localRepository);

    public void start(Stage stage) {
        locais.setAll(localService.listar());

        TableView<Local> tabela = new TableView<>(locais);
        tabela.setPrefHeight(300);

        TableColumn<Local, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Local, String> cidadeCol = new TableColumn<>("Cidade");
        cidadeCol.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        TableColumn<Local, String> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Local, String> cepCol = new TableColumn<>("CEP");
        cepCol.setCellValueFactory(new PropertyValueFactory<>("cep"));

        TableColumn<Local, String> telefoneCol = new TableColumn<>("Telefone");
        telefoneCol.setCellValueFactory(new PropertyValueFactory<>("telefone"));



        // Coluna Editar
        TableColumn<Local, Void> editarCol = new TableColumn<>("Editar");
        editarCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("✏️");
            {
                btn.setOnAction(_-> abrirFormulario(tabela.getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Coluna Excluir
        TableColumn<Local, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setOnAction(_ -> {
                    Local local = tabela.getItems().get(getIndex());

                    ModalConfirmacao.mostrar(
                            "Confirmação",
                            "Tem certeza que deseja excluir o local '" + local.getNome() + "'?",
                            () -> {
                                locais.remove(local);
                                localService.remover(local);
                            }
                    );
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tabela.getColumns().addAll(nomeCol, cidadeCol, estadoCol, cepCol, telefoneCol, editarCol, excluirCol);

        Button novoBtn = new Button("Novo Local");
        novoBtn.setOnAction(_ -> abrirFormulario(null));

        VBox layout = new VBox(10, new Label("Tela de Locais"), tabela, novoBtn);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Locais");
        stage.show();
    }

    private void abrirFormulario(Local local) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(local == null ? "Novo Local" : "Editar Local");

        TextField nomeField = new TextField(local != null ? local.getNome() : "");
        TextField enderecoField = new TextField(local != null ? local.getEndereco() : "");
        TextField cidadeField = new TextField(local != null ? local.getCidade() : "");
        TextField estadoField = new TextField(local != null ? local.getEstado() : "");
        TextField cepField = new TextField(local != null && local.getCep() != null ? local.getCep().toString() : "");
        TextField telefoneField = new TextField(local != null && local.getTelefone() != null ? local.getTelefone().toString() : "");

        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(_ -> {
            try {
                // Monta a mensagem da confirmação dinamicamente
                String nomeLocal = nomeField.getText();
                String mensagem = (local == null)
                        ? "Tem certeza que deseja salvar o local '" + nomeLocal + "'?"
                        : "Tem certeza que deseja atualizar o local '" + nomeLocal + "'?";

                ModalConfirmacao.mostrar(
                        "Confirmação",
                        mensagem,
                        () -> {
                            if (local == null) {
                                int id = localService.gerarNovoId();
                                Local novo = new Local(
                                        id,
                                        nomeField.getText(),
                                        enderecoField.getText(),
                                        cidadeField.getText(),
                                        estadoField.getText(),
                                        Integer.parseInt(cepField.getText()),
                                        Integer.parseInt(telefoneField.getText())
                                );
                                locais.add(novo);
                                localService.adicionar(novo);
                            } else {
                                local.setNome(nomeField.getText());
                                local.setEndereco(enderecoField.getText());
                                local.setCidade(cidadeField.getText());
                                local.setEstado(estadoField.getText());
                                local.setCep(Integer.parseInt(cepField.getText()));
                                local.setTelefone(Integer.parseInt(telefoneField.getText()));
                                // Força a atualização na tabela
                                int index = locais.indexOf(local);
                                locais.set(index, local);
                                localService.atualizar(local);
                            }
                            modal.close();
                        }
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Erro ao salvar: " + ex.getMessage()).show();
            }
        });

        VBox form = new VBox(10,
                new Label("Nome:"), nomeField,
                new Label("Endereço:"), enderecoField,
                new Label("Cidade:"), cidadeField,
                new Label("Estado:"), estadoField,
                new Label("CEP:"), cepField,
                new Label("Telefone:"), telefoneField,
                salvarBtn
        );
        form.setPadding(new Insets(20));

        modal.setScene(new Scene(form, 300, 450));
        modal.showAndWait();
    }
}