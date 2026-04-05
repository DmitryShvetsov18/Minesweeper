package logic;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MinesweeperUtils {
    public static Cell[][] createListOfCells(int rows, int columns, GridPane pane) {
        Cell[][] result = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell button = new Cell(i, j);
                button.setPrefSize(50, 50);
                button.setStyle("-fx-font-size: 20px");
                button.setOnAction(e -> {
                    System.out.println(button);
                    if (button.IsMine()) {
                        button.setText("*");
                        button.setStyle("-fx-background-color: red");
                    }
                    else {
                        button.setText(String.valueOf(button.getNumber()));
                        System.out.println(String.valueOf(button.getNumber()));
                    }
                    button.setDisable(true);
                    button.setTextFill(Color.BLACK);
                });
                pane.add(button, j, i);
                result[i][j] = button;

            }
        }
        return result;
    }
}
