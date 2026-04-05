package logic;


import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MinesweeperUtils {
    public static Cell[][] createListOfCells(int rows, int columns, GridPane pane) {
        Cell[][] result = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell button = new Cell(i, j);
                button.setPrefSize(25, 25);
                button.setOnAction(e -> {
                    System.out.println(button);
                });
                pane.add(button, j, i);
                result[i][j] = button;

            }
        }
        return result;
    }
}
