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
        Minesweeper gameBoard = new Minesweeper(10, 10, 15);
        gameBoard.insertMines();
        for (int i = 0; i < gameBoard.getRows(); i++) {
            for (int j = 0; j < gameBoard.getColumns(); j++) {
                Button button = new Cell(i, j);
                button.setPrefSize(25, 25);
                gridPane.add(button, i, j);
            }
        }
        gameBoard.printCells();
        Scene scene = new Scene(gridPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }
}
