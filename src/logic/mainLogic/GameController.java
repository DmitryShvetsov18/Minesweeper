package logic.mainLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.input.MouseButton;

import java.util.List;

/**
 * Контроллер игры "Сапёр".
 * <p>
 * Координирует взаимодействие между игровым полем ({@link GameBoard})
 * и графическим интерфейсом пользователя ({@link MinesweeperGUI}).
 * Отвечает за обработку действий игрока, обновление отображения клеток,
 * управление таймером игры и завершение партии.
 * </p>
 */
public class GameController {

    private final GameBoard gameBoard;
    private final CellPane[][] cells;
    private final GridPane gridPane;
    private final MinesweeperGUI gui;

    private int flagsPlaced = 0;

    private Timeline timer;
    private int secondsElapsed = 0;

    /**
     * Создает новый контроллер игры.
     *
     * @param rows количество строк игрового поля
     * @param columns количество столбцов игрового поля
     * @param minesCount количество мин на поле
     * @param gridPane контейнер для размещения клеток
     * @param gui главное окно приложения
     */
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

    /**
     * Создает графическое представление игрового поля.
     * <p>
     * Для каждой клетки модели создается соответствующий объект
     * {@link CellPane}, после чего настраиваются обработчики
     * нажатий мыши и размещение компонентов в {@link GridPane}.
     * </p>
     */
    private void setupUI() {
        gridPane.getChildren().clear();

        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(javafx.geometry.Insets.EMPTY);

        for (int y = 0; y < gameBoard.getRows(); y++) {
            for (int x = 0; x < gameBoard.getColumns(); x++) {
                CellPane cellPane = new CellPane(y, x, gameBoard.getCell(y, x));

                cellPane.setMinSize(50, 50);
                cellPane.setMaxSize(50, 50);

                final int row = y, column = x;

                cellPane.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        leftClick(row, column);
                    }
                });

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
    /**
     * Обрабатывает нажатие левой кнопкой мыши по клетке.
     * <p>
     * Выполняет открытие клетки, обновление отображения поля
     * и проверку условий завершения игры.
     * </p>
     *
     * @param row номер строки клетки
     * @param column номер столбца клетки
     */
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

    /**
     * Обновляет отображение всех клеток на поле
     */
    private void updateAllCells() {
        for (int y = 0; y < gameBoard.getRows(); y++)
            for (int x = 0; x < gameBoard.getColumns(); x++)
                cells[y][x].updateDisplay();
    }

    private void updateCell(int row, int column) {
        cells[row][column].updateDisplay();
    }


    /**
     * Обрабатывает нажатие правой кнопкой мыши по клетке.
     * <p>
     * Устанавливает или снимает флаг и обновляет счетчик
     * установленных флагов.
     * </p>
     *
     * @param row номер строки клетки
     * @param column номер столбца клетки
     */
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
