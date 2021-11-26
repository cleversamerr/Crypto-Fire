package com.app;

import com.app.models.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Run extends Application {

    @Override
    public void init() throws Exception {
        Data.importData();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("interfaces/login.fxml")));
        primaryStage.setTitle("V1.0 Crypto Fire");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 790, 490));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Data.exportData();
    }
}
