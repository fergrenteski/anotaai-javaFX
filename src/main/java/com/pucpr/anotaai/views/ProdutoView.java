package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Produtos;
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

    // Mantém a lista observável de produtos
    private final ObservableList<Produtos> produtos = FXCollections.observableArrayList();
    private final ProductRepository productRepository = new ProductRepository();
    private final ProductService productService = new ProductService(productRepository);

    public void start(Stage stage) {
        // Carrega todos os produtos existentes no início
        produtos.setAll(productService.listar());

        TableView<Produtos> tableView = new TableView<>(produtos);

        // Coluna “Nome”
        TableColumn<Produtos, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Coluna “Descrição”
        TableColumn<Produtos, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        // Coluna “Categoria”
        TableColumn<Produtos, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Coluna “Preço”
        TableColumn<Produtos, Double> precoCol = new TableColumn<>("Preço");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Coluna “Quantidade”
        TableColumn<Produtos, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Coluna “Editar”
        TableColumn<Produtos, Void> editarCol = new TableColumn<>("Editar");
        editarCol.setMinWidth(80);
        editarCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Produtos, Void> call(final TableColumn<Produtos, Void> param) {
                return new TableCell<>() {
                    private final Button btnEditar = new Button("✏️");

                    {
                        btnEditar.setOnAction(_e -> {
                            Produtos produtoSelecionado = getTableView().getItems().get(getIndex());
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

        // Coluna “Excluir”
        TableColumn<Produtos, Void> excluirCol = new TableColumn<>("Excluir");
        excluirCol.setMinWidth(80);
        excluirCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Produtos, Void> call(final TableColumn<Produtos, Void> param) {
                return new TableCell<>() {
                    private final Button btnExcluir = new Button("🗑️");

                    {
                        btnExcluir.setOnAction(_e -> {
                            Produtos produtoSelecionado = getTableView().getItems().get(getIndex());
                            productService.remover(produtoSelecionado); // remove do repositório
                            produtos.remove(produtoSelecionado);       // remove da lista exibida
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

        // Adiciona todas as colunas na tabela
        tableView.getColumns().addAll(
                nomeCol,
                descricaoCol,
                categoriaCol,
                precoCol,
                quantidadeCol,
                editarCol,
                excluirCol
        );

        // Botão “Adicionar” (abre o formulário em branco)
        Button adicionar = new Button("Adicionar");
        adicionar.setOnAction(e -> formProduct(null));

        // Botão “Voltar” (fecha a janela atual)
        Button voltar = new Button("Voltar");
        voltar.setOnAction(e -> stage.close());

        HBox hBoxBotoes = new HBox(10, adicionar, voltar);
        hBoxBotoes.setAlignment(Pos.CENTER_RIGHT);
        hBoxBotoes.setPadding(new Insets(10, 0, 0, 0));

        // Cabeçalho da lista de produtos
        Label label = new Label("Lista de Produtos:");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Layout principal da janela
        VBox vboxPrincipal = new VBox(10, label, tableView, hBoxBotoes);
        vboxPrincipal.setPadding(new Insets(10));
        stage.setScene(new Scene(vboxPrincipal, 700, 500));
        stage.setTitle("Produtos");
        stage.show();
    }

    // ---
    private void formProduct(Produtos produto) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(produto == null ? "Novo Produto" : "Editar Produto");

        // Labels e campos de texto
        Label labNome      = new Label("Nome:");
        TextField txtNome      = new TextField( produto != null ? produto.getNome() : "" );

        Label labDescricao = new Label("Descrição:");
        TextField txtDescricao = new TextField( produto != null ? produto.getDescricao() : "" );

        Label labCategoria = new Label("Categoria:");
        TextField txtCategoria = new TextField( produto != null ? produto.getCategoria() : "" );

        Label labPreco     = new Label("Preço:");
        TextField txtPreco     = new TextField( produto != null ? String.valueOf(produto.getPreco()) : "" );

        Label labQuantidade = new Label("Quantidade:");
        TextField txtQuantidade = new TextField( produto != null ? String.valueOf(produto.getQuantidade()) : "" );

        // Botão “Voltar” (fecha o formulário sem salvar)
        Button btnVoltar = new Button("Voltar");
        btnVoltar.setOnAction(e -> stage.close());

        // Botão “Salvar”
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Validações e conversões básicas
            String nomeStr      = txtNome.getText().trim();
            String descStr      = txtDescricao.getText().trim();
            String categStr     = txtCategoria.getText().trim();
            String precoStr     = txtPreco.getText().trim();
            String qtdStr       = txtQuantidade.getText().trim();

            if (produto == null) {
                int id = productService.gerarNovoId();
                Produtos novo = new Produtos(
                        id,
                        nomeStr,
                        descStr,
                        categStr,
                        Double.parseDouble(precoStr),
                        Integer.parseInt(qtdStr)
                );
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
}
