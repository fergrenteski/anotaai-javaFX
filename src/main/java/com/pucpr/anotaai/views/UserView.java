package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.User;
import com.pucpr.anotaai.repository.UserRepository;
import com.pucpr.anotaai.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserView {

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);

    public void start(Stage stage) {
        users.setAll(userService.listar());

        TableView<User> tabela = new TableView<>(users);
        tabela.setPrefHeight(300);

        TableColumn<User, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<User, String> biografiaCol = new TableColumn<>("Biografia");
        biografiaCol.setCellValueFactory(new PropertyValueFactory<>("biografia"));

        TableColumn<User, String> cpfCol = new TableColumn<>("Cpf");
        cpfCol.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> dataNascimentoCol = new TableColumn<>("Data de Nascimento");
        dataNascimentoCol.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));




        // Coluna Editar
        TableColumn<User, Void> editarCol = new TableColumn<>("Editar");
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
        TableColumn<User, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setOnAction(_ -> {
                    User user = tabela.getItems().get(getIndex());

                    ModalConfirmacao.mostrar(
                            "Confirmação",
                            "Tem certeza que deseja excluir o user '" + user.getNome() + "'?",
                            () -> {
                                users.remove(user);
                                userService.remover(user);
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

        tabela.getColumns().addAll(nomeCol, biografiaCol, cpfCol, emailCol, dataNascimentoCol, editarCol, excluirCol);

        Button novoBtn = new Button("Novo Usuários");
        novoBtn.setOnAction(_ -> abrirFormulario(null));

        VBox layout = new VBox(10, new Label("Tela de Usuários"), tabela, novoBtn);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Usuários");
        stage.show();
    }

    private void abrirFormulario(User user) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(user == null ? "Novo Usuário" : "Editar Usuário");

        TextField nomeField = new TextField(user != null ? user.getNome() : "");
        TextField biografiaField = new TextField(user != null ? user.getBiografia() : "");
        TextField cpfField = new TextField(user != null ? user.getCpf() : "");
        TextField emailField = new TextField(user != null ? user.getEmail() : "");
        TextField dataNascimentoField = new TextField(user != null && user.getDataNascimento() != null ? user.getDataNascimento() : "");


        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(_ -> {
            try {
                // Monta a mensagem da confirmação dinamicamente
                String nomeUser = nomeField.getText();
                String mensagem = (user == null)
                        ? "Tem certeza que deseja salvar o user '" + nomeUser + "'?"
                        : "Tem certeza que deseja atualizar o user '" + nomeUser + "'?";

                ModalConfirmacao.mostrar(
                        "Confirmação",
                        mensagem,
                        () -> {
                            if (user == null) {
                                int id = userService.gerarNovoId();
                                User novo = new User(
                                        id,
                                        nomeField.getText(),
                                        biografiaField.getText(),
                                        cpfField.getText(),
                                        emailField.getText(),
                                        dataNascimentoField.getText()
                                );
                                users.add(novo);
                                userService.adicionar(novo);
                            } else {
                                user.setNome(nomeField.getText());
                                user.setBiografia(biografiaField.getText());
                                user.setCpf(cpfField.getText());
                                user.setEmail(emailField.getText());
                                user.setDataNascimento(dataNascimentoField.getText());

                                // Força a atualização na tabela
                                int index = users.indexOf(user);
                                users.set(index, user);
                                userService.atualizar(user);
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
                new Label("Biografia:"), biografiaField,
                new Label("Cpf:"), cpfField,
                new Label("Email:"), emailField,
                new Label("DataNascimento:"), dataNascimentoField,

                salvarBtn
        );
        form.setPadding(new Insets(20));

        modal.setScene(new Scene(form, 300, 450));
        modal.showAndWait();
    }
}
