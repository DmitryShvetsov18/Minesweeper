package logic;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MinesweeperUtils {
    public static Cell[][] createListOfCells(int rows, int columns, GridPane pane) {
        Cell[][] result = new Cell[rows + 2][columns + 2];
        int lastNumberOfCell = 0;
        for (int y = 0; y < rows + 2; y++) {
            for (int x = 0; x < columns + 2; x++) {
                if (y == 0 || x == 0 || y == (rows + 1) || x == (columns + 1)) {
                    Cell button = new Cell(y, x);
                    result[y][x] = button;
                    continue;
                }
                Cell button = new Cell(y, x, lastNumberOfCell++);
                button.setPrefSize(50, 50);
                button.setStyle("-fx-font-size: 20px");
                button.setOnAction(e -> {
                    System.out.println(button);
                    if (button.IsMine()) {
                        button.setText("*");
                        button.setStyle("-fx-background-color: red");
                    }
                    else {
                        button.setText(String.valueOf(button.getCountOfMines()));
                        System.out.println(String.valueOf(button.getNumber()));
                    }
                    button.setDisable(true);
                    button.setTextFill(Color.BLACK);
                });
                pane.add(button, x, y);
                result[y][x] = button;

            }
        }
        return result;
    }
}
