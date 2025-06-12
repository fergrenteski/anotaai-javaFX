package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Lista;
import com.pucpr.anotaai.repository.ListaRepository;
import com.pucpr.anotaai.service.ListaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ListaView {

    private final ObservableList<Lista> listas = FXCollections.observableArrayList();
    private final ListaRepository listaRepository = new ListaRepository();
    private final ListaService listaService = new ListaService(listaRepository);

    public void start(Stage stage) {
        listas.setAll(listaService.listar());

        TableView<Lista> tabela = new TableView<>(listas);
        tabela.setPrefHeight(300);

        TableColumn<Lista, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nomeLista"));

        TableColumn<Lista, String> catCol = new TableColumn<>("Categoria");
        catCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Lista, String> nomeRespCol = new TableColumn<>("Nome Resp.");
        nomeRespCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Lista, String> dateCol = new TableColumn<>("Data Criação.");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));

        // Coluna Editar
        TableColumn<Lista, Void> editarCol = new TableColumn<>("Editar");
        editarCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("Editar");
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
        TableColumn<Lista, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("🗑");
            {
                btn.setOnAction(_ -> {
                    Lista lista = tabela.getItems().get(getIndex());

                    ModalConfirmacao.mostrar(
                            "Confirmação",
                            "Tem certeza que deseja excluir o lista '" + lista.getNome() + "'?",
                            () -> {
                                listas.remove(lista);
                                listaService.remover(lista);
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

        tabela.getColumns().addAll(nomeCol, catCol, nomeRespCol, dateCol, editarCol, excluirCol);

        Button novoBtn = new Button("Nova Lista");
        novoBtn.setOnAction(_ -> abrirFormulario(null));

        VBox layout = new VBox(10, new Label("Tela de Listas"), tabela, novoBtn);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Listas");
        stage.show();
    }

    private void abrirFormulario(Lista lista) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(lista == null ? "Nova Lista" : "Editar Lista");

        TextField nomeField = new TextField(lista != null ? lista.getNomeLista() : "");
        TextField catField = new TextField(lista != null ? lista.getCategoria() : "");
        TextField nomeRespField = new TextField(lista != null ? lista.getNome() : "");

        Button salvarBtn = new Button("Salvar");
        salvarBtn.setOnAction(_ -> {
            try {
                String nome = nomeField.getText().trim();
                String cat = catField.getText().trim();
                String nomeResp = nomeRespField.getText().trim();
                LocalDate date = LocalDate.now();

                String mensagem = (lista == null)
                        ? "Tem certeza que deseja salvar o lista '" + nome + "'?"
                        : "Tem certeza que deseja atualizar o lista '" + nome + "'?";


                if (nome.isEmpty() || cat.isEmpty() || nomeResp.isEmpty()) {
                    throw new IllegalArgumentException("Todos os campos são obrigatórios.");
                }

                ModalConfirmacao.mostrar(
                        "Confirmação",
                        mensagem,
                        () -> {
                            if (lista == null) {
                                int id = listaService.gerarNovoId();
                                Lista novo = new Lista(id, nome, cat, nomeResp, date);
                                listas.add(novo);
                                listaService.adicionar(novo);
                            } else {
                                lista.setNomeLista(nome);
                                lista.setCategoria(cat);
                                lista.setNome(nome);
                                int index = listas.indexOf(lista);
                                listas.set(index, lista);
                                listaService.atualizar(lista);
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
                new Label("Nome:*"), nomeField,
                new Label("Categoria:*"), catField,
                new Label("Nome Resp.:*"), nomeRespField,
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
