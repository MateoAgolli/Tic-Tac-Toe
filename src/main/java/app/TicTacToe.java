package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    @Override
    public void start(Stage window) {
        TicTacToeUI ui = new TicTacToeUI(window);
        ui.start();
    }

    public static void main(String[] args) {
        launch(TicTacToe.class);
    }
}