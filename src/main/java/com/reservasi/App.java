package com.reservasi;

import com.reservasi.util.SceneManager;
import javafx.stage.Stage;

public class App {
    public void start(Stage primaryStage) throws Exception {
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginScene();
    }
}