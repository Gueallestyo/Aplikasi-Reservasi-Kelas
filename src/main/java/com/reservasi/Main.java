package com.reservasi;

import com.reservasi.controller.LoginController;
import com.reservasi.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager sceneManager = new SceneManager(primaryStage);
        
        // Load login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservasi/view/login.fxml"));
        Parent root = loader.load();
        
        // Dapatkan controller dan set sceneManager
        LoginController loginController = loader.getController();
        loginController.setSceneManager(sceneManager);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reservasi Kelas");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}