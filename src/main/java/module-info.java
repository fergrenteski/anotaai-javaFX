module com.pucpr.anotaai {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports com.pucpr.anotaai.app;
    exports com.pucpr.anotaai.views;
    exports com.pucpr.anotaai.navigation;

    opens com.pucpr.anotaai.model to javafx.base;

}