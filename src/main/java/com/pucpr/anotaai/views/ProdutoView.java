package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Produto;
import com.pucpr.anotaai.repository.ProductRepository;
import com.pucpr.anotaai.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ProdutoView {

    private final ObservableList<Produto> produtos = FXCollections.observableArrayList();
    private final ProductRepository productRepository = new ProductRepository();
    private final ProductService productService = new ProductService(productRepository);

    public void start(Stage stage) {
        produtos.setAll(productService.listar());

        TableView<Produto> tableView = new TableView<>(produtos);

        TableColumn<Produto, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Produto, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Produto, Double> precoCol = new TableColumn<>("Preço");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produto, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Produto, Void> editarCol = new TableColumn<>("Editar");
        editarCol.setMinWidth(80);
        editarCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Produto, Void> call(final TableColumn<Produto, Void> param) {
                return new TableCell<>() {
                    private final Button btnEditar = new Button("✏️");

                    {
                        btnEditar.setOnAction(_e -> {
                            Produto produtoSelecionado = getTableView().getItems().get(getIndex());
                            formProduct(produtoSelecionado);
                        });
                        btnEditar.setMaxWidth(Double.MAX_VALUE);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnEditar);
                    }
                };
            }
        });

        TableColumn<Produto, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setMinWidth(80);
        excluirCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Produto, Void> call(final TableColumn<Produto, Void> param) {
                return new TableCell<>() {
                    private final Button btnExcluir = new Button("🗑️");

                    {
                        btnExcluir.setOnAction(_e -> {
                            Produto produtoSelecionado = getTableView().getItems().get(getIndex());
                            productService.remover(produtoSelecionado);
                            produtos.remove(produtoSelecionado);
                        });
                        btnExcluir.setMaxWidth(Double.MAX_VALUE);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnExcluir);
                    }
                };
            }
        });

        tableView.getColumns().addAll(
                nomeCol,
                descricaoCol,
                categoriaCol,
                precoCol,
                quantidadeCol,
                editarCol,
                excluirCol
        );

        Button adicionar = new Button("Adicionar");
        adicionar.setOnAction(e -> formProduct(null));

        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> stage.close());

        HBox hBoxBotoes = new HBox(10, adicionar, voltar);
        hBoxBotoes.setAlignment(Pos.CENTER_RIGHT);
        hBoxBotoes.setPadding(new Insets(10, 0, 0, 0));

        // Cabeçalho da lista de produtos
        Label label = new Label("Lista de Produto:");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Layout principal da janela
        VBox vboxPrincipal = new VBox(10, label, tableView, hBoxBotoes);
        vboxPrincipal.setPadding(new Insets(10));
        stage.setScene(new Scene(vboxPrincipal, 700, 500));
        stage.setTitle("Produto");
        stage.show();
    }

    // ---
    private void formProduct(Produto produto) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(produto == null ? "Novo Produto" : "Editar Produto");

        // Labels e campos de texto
        Label labNome      = new Label("Nome:*");
        TextField txtNome      = new TextField( produto != null ? produto.getNome() : "" );

        Label labDescricao = new Label("Descrição:*");
        TextField txtDescricao = new TextField( produto != null ? produto.getDescricao() : "" );

        Label labCategoria = new Label("Categoria:*");
        TextField txtCategoria = new TextField( produto != null ? produto.getCategoria() : "" );

        Label labPreco     = new Label("Preço:*");
        TextField txtPreco     = new TextField( produto != null ? String.valueOf(produto.getPreco()) : "" );

        Label labQuantidade = new Label("Quantidade:*");
        TextField txtQuantidade = new TextField( produto != null ? String.valueOf(produto.getQuantidade()) : "" );

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> {
            String nomeStr      = txtNome.getText().trim();
            String descStr      = txtDescricao.getText().trim();
            String categStr     = txtCategoria.getText().trim();
            String precoStr     = txtPreco.getText().trim();
            String qtdStr       = txtQuantidade.getText().trim();

            // se algum estiver não vazio, pergunta antes de descartar
            boolean anyFilled = !nomeStr.isEmpty()
                    || !descStr.isEmpty()
                    || !categStr.isEmpty()
                    || !precoStr.isEmpty()
                    || !qtdStr.isEmpty();

            if (anyFilled) {
                ModalConfirmacao.mostrar(
                        "Atenção",
                        "Deseja descartar as alterações?",
                        () -> stage.close()
                );
            } else {
                stage.close();
            }
        });

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nomeStr      = txtNome.getText().trim();
            String descStr      = txtDescricao.getText().trim();
            String categStr     = txtCategoria.getText().trim();
            String precoStr     = txtPreco.getText().trim();
            String qtdStr       = txtQuantidade.getText().trim();

            if (nomeStr.isEmpty() || descStr.isEmpty() || categStr.isEmpty() || precoStr.isEmpty() || qtdStr.isEmpty()) {
                String msg =
                        "Preencha o(s) campo(s): " + (nomeStr.isEmpty() ? "Nome" : "")
                                + (descStr.isEmpty() ? (nomeStr.isEmpty() ? ", " : "") + "Descrição" : "")
                                + (categStr.isEmpty() ? ((nomeStr.isEmpty() || descStr.isEmpty()) ? ", " : "") + "Categoria" : "")
                                + (precoStr.isEmpty() ? ((nomeStr.isEmpty() || descStr.isEmpty() || categStr.isEmpty()) ? ", " : "") + "Preço" : "")
                                + (qtdStr.isEmpty() ? ((nomeStr.isEmpty() || descStr.isEmpty() || categStr.isEmpty() || precoStr.isEmpty()) ? ", " : "") + "Quantidade" : "");
                exibirErro(msg);
                throw new IllegalArgumentException();
            }

            if (Double.parseDouble(precoStr) <= 0 || Double.parseDouble(qtdStr) <= 0) {
                String msg = "O(s) campo(s) precisam ser maior(es) que zero: "
                        + (Double.parseDouble(precoStr) <= 0 ? "Preço" : "")
                        + (Double.parseDouble(qtdStr) <= 0 ? (Double.parseDouble(precoStr) <= 0 ? ", Quantidade" : "Quantidade") : "");
                exibirErro(msg);
                throw new IllegalArgumentException();
            }

            String pergunta = produto == null
                    ? "Tem certeza que deseja salvar o produto '" + nomeStr + "'?"
                    : "Tem certeza que deseja atualizar o produto '" + nomeStr + "'?";

            ModalConfirmacao.mostrar(
                    "Confirmação",
                    pergunta,
                    () -> {
                        if (produto == null) {
                            int id = productService.gerarNovoId();
                            Produto novo = new Produto(
                                    id,
                                    nomeStr,
                                    descStr,
                                    categStr,
                                    Double.parseDouble(precoStr),
                                    Integer.parseInt(qtdStr));
                            productService.adicionar(novo);
                            produtos.add(novo);
                        } else {
                            produto.setNome(nomeStr);
                            produto.setDescricao(descStr);
                            produto.setCategoria(categStr);
                            produto.setPreco(Double.parseDouble(precoStr));
                            produto.setQuantidade(Integer.parseInt(qtdStr));
                            productService.atualizar(produto);
                            int idx = produtos.indexOf(produto);
                            produtos.set(idx, produto);
                        }
                        stage.close();
                    }
            );
        });

        VBox vbox = new VBox(8,
                labNome, txtNome,
                labDescricao, txtDescricao,
                labCategoria, txtCategoria,
                labPreco, txtPreco,
                labQuantidade, txtQuantidade
        );
        vbox.setPadding(new Insets(10));

        HBox hbox = new HBox(10, btnSalvar, btnVoltar);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(10, 0, 0, 0));

        VBox form = new VBox(10, vbox, hbox);
        form.setPadding(new Insets(10));

        stage.setScene(new Scene(form, 350, 400));
        stage.showAndWait();
    }
    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Validação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
