package logic;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

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
                        button.setStyle(
                            "-fx-background-color: red; -fx-font-size: 20px"
                        );
                    }
                    else {
                        button.setText(String.valueOf(button.getCountOfMines()));
                        String color = colors.get(button.getCountOfMines());
                        button.setStyle("-fx-text-fill: %s; -fx-font-size: 24px; font-weight: bold".formatted(color));
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
    public static HashMap<Integer, String> colors = new HashMap<>(
        Map.ofEntries(
            Map.entry(1, "blue"),
            Map.entry(2, "green"),
            Map.entry(3, "red"),
            Map.entry(4, "purple"),
            Map.entry(5, "brown"),
            Map.entry(6, "cyan"),
            Map.entry(7, "orange"),
            Map.entry(8, "magenta")
        )
    );

}
