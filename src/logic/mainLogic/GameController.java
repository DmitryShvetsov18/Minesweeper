package logic.mainLogic;

import javafx.scene.layout.GridPane;

public class GameController {
    private final GameBoard gameBoard;
    private final CellButton[][] cells;
    private final GridPane gridPane;

    public GameController(int rows, int columns, int minesCount, GridPane gridPane) {
        this.gameBoard = new GameBoard(rows, columns, minesCount);
        this.cells = new CellButton[rows][columns];
        this.gridPane = gridPane;
        setupUI();
    }

    private void setupUI() {
        gridPane.getChildren().clear();

        for (int y = 0; y < gameBoard.getColumns(); y++) {
            for (int x = 0; x < gameBoard.getRows(); x++) {
                CellButton cellButton = new CellButton(y, x, gameBoard.getCell(y, x));
                final int row = y;
                final int column = x;

                cellButton.setOnAction(e -> leftClick(row, column));
                cellButton.setOnContextMenuRequested(e -> rightClick(row, column));

                gridPane.add(cellButton, x, y);
                cells[y][x] = cellButton;
            }
        }
    }

    private void leftClick(int row, int column) {
        if (gameBoard.isGameOver()) { return; }

        gameBoard.openCell(row, column);
        updateAllCells();
        if (gameBoard.isGameOver()) {
            if (gameBoard.isWin()) {
                System.out.println("Победа");
            }
            else {
                System.out.println("Поражение");
                gameBoard.openAllMines();
                updateAllCells();
            }
        }

    }

    private void rightClick(int row, int column) {
        if (gameBoard.isGameOver()) {
            return;
        }

        gameBoard.toggleFlag(row, column);
        updateCell(row, column);
    }

    private void updateAllCells() {
        for (int y = 0; y < gameBoard.getRows(); y++) {
            for (int x = 0; x < gameBoard.getColumns(); x++) {
                cells[y][x].updateDisplay();
            }
        }
    }

    private void updateCell(int row, int column) {
        cells[row][column].updateDisplay();
    }

    public void printBoard() {
        gameBoard.printBoard();
    }
}
