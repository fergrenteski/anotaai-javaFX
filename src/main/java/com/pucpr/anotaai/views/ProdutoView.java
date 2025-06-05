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

public class ProdutoView {

    // ObservableList que alimenta a TableView
    private final ObservableList<Produtos> produtos = FXCollections.observableArrayList();

    // Repository e Service (que persiste em TXT)
    private final ProductRepository productRepository = new ProductRepository();
    private final ProductService productService = new ProductService(productRepository);

    public void start(Stage stage) {
        Label header = new Label("Lista de Produtos");
        header.setAlignment(Pos.CENTER);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 1) Cria a TableView e define as colunas
        TableView<Produtos> table = new TableView<>();
        table.setItems(produtos); // vincula a ObservableList à tabela

        TableColumn<Produtos, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produtos, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Produtos, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Produtos, Double> precoCol = new TableColumn<>("Preço");
        precoCol.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Produtos, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        table.getColumns().addAll(nomeCol, descricaoCol, categoriaCol, precoCol, quantidadeCol);

        // 2) Carrega itens já existentes do TXT via service
        produtos.setAll(productService.listar());

        // 3) Botões “Voltar” e “Criar Produto”
        Button btnVoltar = new Button("Voltar");
        Button btnCriar = new Button("Criar Produto");

        btnVoltar.setOnAction(e -> stage.close());
        btnCriar.setOnAction(e -> form(null, table));

        // 4) Barra de botões, alinhada à direita
        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(0, 0, 10, 0));
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.getChildren().addAll(btnCriar, btnVoltar);

        // 5) Layout principal
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(header, table, buttonBar);

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Produtos");
        stage.show();
    }

    /**
     * Abre um formulário modal para cadastro ou edição de produto.
     * Ao clicar em "Salvar", cria/atualiza um Produtos e persiste via ProductService (TXT).
     * Não há validação de campos.
     */
    private void form(Produtos produto, TableView<Produtos> table) {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle(produto == null ? "Novo Produto" : "Editar Produto");

        // Campos de texto
        Label lblNome = new Label("Nome:");
        TextField tfNome = new TextField(produto != null ? produto.getNome() : "");

        Label lblDescricao = new Label("Descrição:");
        TextField tfDescricao = new TextField(produto != null ? produto.getDescricao() : "");

        Label lblCategoria = new Label("Categoria:");
        TextField tfCategoria = new TextField(produto != null ? produto.getCategoria() : "");

        Label lblPreco = new Label("Preço:");
        TextField tfPreco = new TextField(produto != null ? String.valueOf(produto.getPreco()) : "");

        Label lblQuantidade = new Label("Quantidade:");
        TextField tfQuantidade = new TextField(produto != null ? String.valueOf(produto.getQuantidade()) : "");

        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                lblNome, tfNome,
                lblDescricao, tfDescricao,
                lblCategoria, tfCategoria,
                lblPreco, tfPreco,
                lblQuantidade, tfQuantidade
        );

        // HBox para os botões, alinhado à direita
        HBox buttonBarForm = new HBox(10);
        buttonBarForm.setPadding(new Insets(10, 0, 0, 0));
        buttonBarForm.setAlignment(Pos.CENTER_RIGHT);
        buttonBarForm.getChildren().addAll(btnSalvar, btnCancelar);
        root.getChildren().add(buttonBarForm);

        // Ação do botão “Salvar”
        btnSalvar.setOnAction(e -> {
            String nome      = tfNome.getText();
            String descricao = tfDescricao.getText();
            String categoria = tfCategoria.getText();
            double preco     = Double.parseDouble(tfPreco.getText());
            int quantidade   = Integer.parseInt(tfQuantidade.getText());

            if (produto == null) {
                // Novo produto: gera ID via ProductService e adiciona
                int id = productService.gerarNovoId();
                Produtos novo = new Produtos(id, nome, descricao, categoria, preco, quantidade);
                productService.adicionar(novo);  // Persiste em "produtos.txt"
                produtos.add(novo);
            } else {
                // Edição: atualiza campos e persiste
                produto.setNome(nome);
                produto.setDescricao(descricao);
                produto.setCategoria(categoria);
                produto.setPreco(preco);
                produto.setQuantidade(quantidade);
                productService.atualizar(produto);  // Atualiza em "produtos.txt"

                // Atualiza a ObservableList
                int index = produtos.indexOf(produto);
                if (index >= 0) {
                    produtos.set(index, produto);
                }
            }

            // Garante que a tabela reflita as mudanças
            table.refresh();
            formStage.close();
        });

        btnCancelar.setOnAction(e -> formStage.close());

        formStage.setScene(new Scene(root, 600, 400));
        formStage.showAndWait();
    }
}
