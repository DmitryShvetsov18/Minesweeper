package logic;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.*;

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
                    button.setStatus(CellStatus.OPENED);
                    if (button.getStatus() == CellStatus.BOMB) {
                        button.setText("*");
                        button.setStyle(
                            "-fx-background-color: red; -fx-font-size: 20px"
                        );
                    }
                    else if (button.getCountOfMines() != 0){
                        button.setText(String.valueOf(button.getCountOfMines()));
                        String color = colors.get(button.getCountOfMines());
                        button.setStyle("-fx-text-fill: %s; -fx-font-size: 24px; font-weight: bold".formatted(color));

                    }
                    else {
                        button.setText("");
                        openBFS(result, button);
                    }
                    button.setDisable(true);
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
    public static void openBFS(Cell[][] cells, Cell startButton) {
        HashSet<Cell> checkedButtons = new HashSet<>();
        Queue<Cell> queue = new LinkedList<Cell>();
        queue.offer(startButton);

        while (!queue.isEmpty()) {
            Cell button = queue.poll();
            int row = button.getRow();
            int column = button.getColumn();

            for (int y = row - 1; y < row + 2; y++) {
                for (int x = column - 1; x < column + 2; x++) {
                    if (x != column || y != row) {
                        Cell currentNeighbour = cells[y][x];
                        if (currentNeighbour.getCountOfMines() == 0 &&
                            !checkedButtons.contains(currentNeighbour) &&
                                currentNeighbour.getStatus() == CellStatus.DEFAULT &&
                                !queue.contains(currentNeighbour)
                        ) {
                            queue.offer(currentNeighbour);
                        }
                        else if (currentNeighbour.getCountOfMines() > 0 &&
                            currentNeighbour.getStatus() == CellStatus.DEFAULT
                        ) {
                            currentNeighbour.setText(
                                String.valueOf(currentNeighbour.getCountOfMines())
                            );
                            String color = colors.get(currentNeighbour.getCountOfMines());
                            currentNeighbour.setStyle(
                                "-fx-text-fill: %s; -fx-font-size: 24px; font-weight: bold".formatted(color)
                            );
                            currentNeighbour.setDisable(true);
                            currentNeighbour.setStatus(CellStatus.OPENED);
                        }
                    }
                }
            }
            button.setText("");
            button.setDisable(true);
            button.setStatus(CellStatus.OPENED);
            checkedButtons.add(button);
        }
    }
}

