package com.pucpr.anotaai.app;

import com.pucpr.anotaai.navigation.MenuFactory;
import com.pucpr.anotaai.navigation.NavegadorImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Anota Aí - Sistema de Gerenciamento");

        NavegadorImpl navegador = new NavegadorImpl();
        MenuFactory menuFactory = new MenuFactory(navegador);

        BorderPane layout = new BorderPane();
        layout.setTop(menuFactory.criarMenu());

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
