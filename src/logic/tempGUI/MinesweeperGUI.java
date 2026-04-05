package logic.tempGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Minesweeper;
import logic.Cell;

public class MinesweeperGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();
        Minesweeper gameBoard = new Minesweeper(10, 10, 15, gridPane);
        gameBoard.insertMines();

        gameBoard.printCells();
        Scene scene = new Scene(gridPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }
}
