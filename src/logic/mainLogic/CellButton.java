package logic.mainLogic;

import javafx.scene.control.Button;

import java.util.Map;


public class CellButton extends Button {
    private final int row;
    private final int column;
    private final Cell cell;

    private static final Map<Integer, String> COLORS = Map.of(
        1, "blue",
        2, "green",
        3, "red",
        4, "purple",
        5, "brown",
        6, "cyan",
        7, "orange",
        8, "magenta"
    );

    public CellButton(int row, int column, Cell cell) {
        this.row = row;
        this.column = column;
        this.cell = cell;

        setPrefSize(50, 50);
        setStyle("-fx-font-size: 20px");
        updateDisplay();
    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public Cell getCell() {
        return cell;
    }

    public void updateDisplay() {
        if (cell.IsOpen()) {
            if (cell.IsMine()) {
                setText("*");
                setStyle("-fx-background-color: red; -fx-font-size: 20px");
            }
            else {
                int countNeighbourMines = cell.getCountOfNeighbourMines();
                if (countNeighbourMines > 0) {
                    setText(String.valueOf(countNeighbourMines));
                    String color = COLORS.get(countNeighbourMines);
                    setStyle(String.format(
                            "-fx-text-fill: %s; -fx-font-size: 24px; -fx-font-weight: bold",
                            color
                    ));
                } else {
                    setText("");
                    setStyle("-fx-font-size: 20px");
                }
            }
            setDisable(true);
        } else if (cell.IsFlagged()) {
            setText("⚑");
            setStyle("-fx-text-fill: red; -fx-font-size: 24px");
            setDisable(false);
        } else {
            setText("");
            setStyle("-fx-font-size: 20px");
            setDisable(false);
        }
    }
}
