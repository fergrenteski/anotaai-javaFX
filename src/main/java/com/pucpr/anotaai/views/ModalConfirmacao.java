package com.pucpr.anotaai.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalConfirmacao {

    /**
     * Mostra um modal de confirmação com a mensagem e executa a ação confirmada se o usuário clicar em "Sim".
     *
     * @param titulo título da janela
     * @param mensagem texto da mensagem a ser exibida
     * @param aoConfirmar Runnable com a ação a executar ao confirmar
     */
    public static void mostrar(String titulo, String mensagem, Runnable aoConfirmar) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle(titulo);

        Label labelMensagem = new Label(mensagem);

        Button btnSim = new Button("Sim");
        btnSim.setOnAction(_ -> {
            aoConfirmar.run();
            modal.close();
        });

        Button btnNao = new Button("Não");
        btnNao.setOnAction(_ -> modal.close());

        HBox botoes = new HBox(10, btnSim, btnNao);
        botoes.setPadding(new Insets(10));
        botoes.setStyle("-fx-alignment: center;");

        VBox layout = new VBox(10, labelMensagem, botoes);
        layout.setPadding(new Insets(20));

        modal.setScene(new Scene(layout, 350, 120));
        modal.showAndWait();
    }
}
