package logic.mainLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.input.MouseButton;

import java.util.List;

public class GameController {

    private final GameBoard gameBoard;
    private final CellPane[][] cells;
    private final GridPane gridPane;
    private final MinesweeperGUI gui;


    private Timeline timer;
    private int secondsElapsed = 0;

    public GameController(int rows, int columns, int minesCount, GridPane gridPane,
            MinesweeperGUI gui) {
        this.gameBoard = new GameBoard(rows, columns, minesCount);
        this.cells = new CellPane[rows][columns];
        this.gridPane = gridPane;
        this.gui = gui;
        setupUI();
        startTimer();
    }

    public int getRows() {
        return gameBoard.getRows();
    }

    public int getColumns() {
        return gameBoard.getColumns();
    }

    public int getMines() {
        return gameBoard.getMinesCount();
    }

    public List<Integer> getMinesPositions() {
        return gameBoard.getMinesPositions();
    }

    private void setupUI() {
        gridPane.getChildren().clear();

        // Важно: убираем отступы GridPane, так как они теперь в контейнере
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(javafx.geometry.Insets.EMPTY);

        for (int y = 0; y < gameBoard.getRows(); y++) {
            for (int x = 0; x < gameBoard.getColumns(); x++) {
                CellPane cellPane = new CellPane(y, x, gameBoard.getCell(y, x));

                // Фиксируем размер
                cellPane.setMinSize(50, 50);
                cellPane.setMaxSize(50, 50);

                final int row = y, column = x;

                // Левый клик
                cellPane.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        leftClick(row, column);
                    }
                });

                // Правый клик
                cellPane.setOnMousePressed(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        rightClick(row, column);
                        event.consume();
                    }
                });

                gridPane.add(cellPane, x, y);
                cells[y][x] = cellPane;
            }
        }
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> secondsElapsed++));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null)
            timer.stop();
    }

    private void leftClick(int row, int column) {
        if (gameBoard.isGameOver())
            return;

        gameBoard.openCell(row, column);
        updateAllCells();

        if (gameBoard.isGameOver()) {
            stopTimer();
            if (gameBoard.isWin()) {
                gui.showWin(secondsElapsed);
            } else {
                gameBoard.openAllMines();
                updateAllCells();
                gui.showLose(secondsElapsed);
            }
        }
    }

    private void updateAllCells() {
        for (int y = 0; y < gameBoard.getRows(); y++)
            for (int x = 0; x < gameBoard.getColumns(); x++)
                cells[y][x].updateDisplay();
    }

    private void updateCell(int row, int column) {
        cells[row][column].updateDisplay();
    }

    private int flagsPlaced = 0;

    private void rightClick(int row, int column) {
        if (gameBoard.isGameOver())
            return;
        boolean wasFlagged = gameBoard.getCell(row, column).IsFlagged();
        gameBoard.toggleFlag(row, column);
        boolean isFlagged = gameBoard.getCell(row, column).IsFlagged();

        if (!wasFlagged && isFlagged)
            flagsPlaced++;
        else if (wasFlagged && !isFlagged)
            flagsPlaced--;

        gui.updateFlagCount(flagsPlaced, gameBoard.getMinesCount());
        updateCell(row, column);
    }
}
