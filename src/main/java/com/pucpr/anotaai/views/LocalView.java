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
        TextField cepField = new TextField(local != null ? local.getCep() : "");
        TextField telefoneField = new TextField(local != null ? local.getTelefone() : "");

        // Máscara simples: permite apenas números
        cepField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,8}")) {
                cepField.setText(oldVal);
            }
        });

        telefoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,11}")) {
                telefoneField.setText(oldVal);
            }
        });

        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(_ -> {
            try {
                String nome = nomeField.getText().trim();
                String endereco = enderecoField.getText().trim();
                String cidade = cidadeField.getText().trim();
                String estado = estadoField.getText().trim();
                String cep = cepField.getText().trim();
                String telefone = telefoneField.getText().trim();

                if (nome.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
                    throw new IllegalArgumentException("Nome, cidade e estado são obrigatórios.");
                }

                if (!cep.matches("\\d{8}")) {
                    throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos.");
                }

                if (!telefone.matches("\\d{10,11}")) {
                    throw new IllegalArgumentException("Telefone deve conter 10 ou 11 dígitos.");
                }

                String mensagem = (local == null)
                        ? "Tem certeza que deseja salvar o local '" + nome + "'?"
                        : "Tem certeza que deseja atualizar o local '" + nome + "'?";

                ModalConfirmacao.mostrar(
                        "Confirmação",
                        mensagem,
                        () -> {
                            if (local == null) {
                                int id = localService.gerarNovoId();
                                Local novo = new Local(id, nome, endereco, cidade, estado, cep, telefone);
                                locais.add(novo);
                                localService.adicionar(novo);
                            } else {
                                local.setNome(nome);
                                local.setEndereco(endereco);
                                local.setCidade(cidade);
                                local.setEstado(estado);
                                local.setCep(cep);
                                local.setTelefone(telefone);
                                int index = locais.indexOf(local);
                                locais.set(index, local);
                                localService.atualizar(local);
                            }
                            modal.close();
                        }
                );

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Erro: " + ex.getMessage()).show();
            }
        });

        VBox form = new VBox(10,
                new Label("Nome:"), nomeField,
                new Label("Endereço:"), enderecoField,
                new Label("Cidade:"), cidadeField,
                new Label("Estado:"), estadoField,
                new Label("CEP (somente números, 8 dígitos):"), cepField,
                new Label("Telefone (somente números, 10 ou 11 dígitos):"), telefoneField,
                salvarBtn
        );
        form.setPadding(new Insets(20));

        modal.setScene(new Scene(form, 320, 480));
        modal.showAndWait();


    }
    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Validação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}