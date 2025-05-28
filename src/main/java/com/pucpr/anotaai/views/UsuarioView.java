package com.pucpr.anotaai.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UsuarioView {
    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("Tela de Usuário");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label info = new Label("Aqui ficará o CRUD de Usuários.");

        layout.getChildren().addAll(title, info);

        stage.setScene(new Scene(layout, 400, 300));
        stage.setTitle("Usuários");
        stage.show();
    }
}
