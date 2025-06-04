package com.pucpr.anotaai.views;

import com.pucpr.anotaai.model.Produtos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProdutoView {
    public void start(Stage stage) {

        Label header = new Label("Lista de Produtos");
        header.setAlignment(Pos.CENTER);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // 2) Cria a TableView
        TableView<Produtos> table = new TableView<>();

        TableColumn<Produtos, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNome())
        );

        TableColumn<Produtos, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao())
        );

        TableColumn<Produtos, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoria())
        );

        TableColumn<Produtos, Number> precoCol = new TableColumn<>("Preço");
        precoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPreco())
        );

        TableColumn<Produtos, Number> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantidade())
        );

        table.getColumns().addAll(nomeCol, descricaoCol, categoriaCol, precoCol, quantidadeCol);

        // 3) Dados de exemplo
        ObservableList<Produtos> produtos = FXCollections.observableArrayList(
                new Produtos("Arroz", "Pacote 5kg", "Alimentos", 22.99, 2),
                new Produtos("Detergente", "500ml", "Limpeza", 3.49, 5)
        );
        table.setItems(produtos);

        Button btnVoltar = new Button("Voltar");
        Button btnCriar = new Button("Criar Produto");
        btnVoltar.setOnAction(e -> stage.close());
        btnCriar.setOnAction(e -> CreateProduct());


        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10, 10, 10, 10));
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.getChildren().addAll(btnCriar, btnVoltar);

        // 5) Monta o layout principal (VBox)
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(header, table, buttonBar);

        stage.setScene(new Scene(layout, 600, 400));
        stage.setTitle("Produtos");
        stage.show();
    }

    private void CreateProduct() {

    }
}
