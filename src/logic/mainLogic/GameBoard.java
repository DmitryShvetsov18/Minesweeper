package logic.mainLogic;


import java.util.*;
import java.util.stream.IntStream;

/**
 * Представляет виртуальное игровое поле сапёра.
 * Отвечает за хранение клеток, генерацию мин,
 * открытие клеток и проверку условий победы.
 *
 * <p>Для избежания выхода за границы поля используются барьерные элементы</p>
 */
public class GameBoard {
    private final int rows;
    private final int columns;
    private final int minesCount;
    private final Cell[][] cells;
    private boolean gameOver;
    private boolean win;
    private int openedCellsCount;
    private boolean firstClick;

    /**
     * Создает новое игровое поле
     *
     * @param rows количество рядов поля
     * @param columns количество колон поля
     * @param minesCount количество мин на поле
     */
    public GameBoard(int rows, int columns, int minesCount) {
        this.rows = rows;
        this.columns = columns;
        this.minesCount = minesCount;
        this.cells = new Cell[rows+2][columns+2];
        this.gameOver = false;
        this.win = false;
        this.openedCellsCount = 0;
        this.firstClick = true;

        initializeCells();
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public int getMinesCount() {
        return minesCount;
    }
    public Cell getCell(int y, int x) {
        return cells[y+1][x+1];
    }
    public boolean isWin() {
        return win;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Заполняет матрицу игрового поля
     */
    private void initializeCells() {
        int cellNumber = 0;
        for (int y = 0; y < rows + 2; y++) {
            for (int x = 0; x < columns + 2; x++) {
                if (x == 0 || y == 0 || x == columns + 1 || y == rows + 1) {
                    cells[y][x] = new Cell(y, x, -1);
                    continue;
                }
                cells[y][x] = new Cell(y, x, cellNumber++);

            }
        }
    }

    /**
     * Заполняет игровое поле минами, избегая первую нажатую кнопку и её соседей
     * @param firstRow номер ряда первой нажатой клетки
     * @param firstColumn номер колонки первой нажатой клетки
     */
    public void insertMines(int firstRow, int firstColumn) {
        Set<Integer> minesPlaces = new HashSet<>(generateMinesPlaces(firstRow, firstColumn));
        for (int i = 1; i < rows + 1; i++) {
            for (int j = 1; j < columns + 1; j++) {
                if (minesPlaces.contains(cells[i][j].getNumber())) {
                    cells[i][j].setIsMine(true);
                }
            }
        }
    }

    /**
     * Генерирует номера ячеек, в которые будут размещены мины,
     * игнорируя номера первой нажатой кнопки и её соседей
     * @param firstRow номер ряда первой нажатой клетки
     * @param firstColumn номер колонки первой нажатой клетки
     * @return Список номеров ячеек, в которые нужно разместить мины
     */
    private List<Integer> generateMinesPlaces(int firstRow, int firstColumn) {
        Set<Integer> forbiddenCells = getForbiddenCells(firstRow, firstColumn);
        int countOfCells = rows * columns;
        List<Integer> indexes = new ArrayList<>(
                IntStream.range(0, countOfCells)
                        .filter(i -> !forbiddenCells.contains(i))
                        .boxed()
                        .toList()
        );
        Collections.shuffle(indexes);

        return indexes.subList(0, this.getMinesCount());
    }

    /**
     * Возвращает множество номеров, в которых не должно быть мин
     * (первая нажатая клетка и её соседи)
     * @param firstRow
     * @param firstColumn
     * @return множество номеров, в которых не должно быть мин
     */
    private Set<Integer> getForbiddenCells(int firstRow, int firstColumn) {
        Set<Integer> forbiddenCells = new HashSet<>();
        int centerY = firstRow + 1;
        int centerX = firstColumn + 1;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                forbiddenCells.add(cells[centerY + dy][centerX + dx].getNumber());
            }
        }
        return forbiddenCells;
    }

    /**
     * Считает количество соседей-мин для каждой клетки
     */
    public void calculateNeighbourMines() {
        for (int y = 1; y < rows + 1; y++) {
            for (int x = 1; x < columns + 1; x++) {
                int count = 0;

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (cells[y + dy][x + dx].IsMine()) {
                            count++;
                        }
                    }
                }
                cells[y][x].setCountOfNeighbourMines(count);
            }
        }
    }

    /**
     * Обрабатывает открытие клетки, если игра ещё не закончена
     * @param row номер ряда открытой ячейки
     * @param column номер колонки открытой ячейки
     */
    public void openCell(int row, int column) {
        if (firstClick) {
            insertMines(row, column);
            calculateNeighbourMines();
            firstClick = false;
        }
        if (gameOver || win) {
            return;
        }

        Cell cell = cells[row+1][column+1];
        if (cell.IsOpen() || cell.IsFlagged()) {
            return;
        }
        if (cell.IsMine()) {
            gameOver = true;
            return;
        }
        openEmptyCells(row, column);
        checkWin();
    }

    /**
     * При помощи алгоритма поиска в ширину, открывает ячейки с 0
     * количеством соседей-мин и их соседей
     * @param row номер ряда открытой ячейки
     * @param column номер колонки открытой ячейки
     */
    public void openEmptyCells(int row, int column) {
        Queue<Cell> queue = new LinkedList<>();
        Set<Cell> visitedCells = new HashSet<>();

        queue.offer(cells[row + 1][column + 1]);

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();
            int currentRow = currentCell.getRow();
            int currentColumn = currentCell.getColumn();
            if (visitedCells.contains(currentCell)) {
                continue;
            }

            if (!currentCell.IsOpen()) {
                currentCell.open();
                openedCellsCount++;
            }
            visitedCells.add(currentCell);

            if (currentCell.getCountOfNeighbourMines() == 0) {
                addNeighboursToQueue(currentRow, currentColumn, queue, visitedCells);
            }
        }
    }

    public void addNeighboursToQueue(int currentRow, int currentColumn, Queue<Cell> queue, Set<Cell> visitedCells) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                Cell currentNeighbour = cells[currentRow + dy][currentColumn + dx];
                if (!currentNeighbour.IsOpen() &&
                        !visitedCells.contains(currentNeighbour) &&
                        !currentNeighbour.IsFlagged() &&
                        !currentNeighbour.IsBarrier() &&
                        !currentNeighbour.IsMine()
                ) {
                    queue.offer(currentNeighbour);
                }
            }
        }
    }

    /**
     * Открывает все ячейки, которые являются минами
     */
    public void openAllMines() {
        for (int y = 1; y < rows + 1; y++) {
            for (int x = 1; x < columns; x++) {
                if (cells[y][x].IsMine()) {
                    cells[y][x].open();
                }
            }
        }
    }

    /**
     * Меняет состояние флага у ячейки, если игра ещё не закончена
     * @param row номер ряда нажатой ячейки
     * @param column номер колонки нажатой ячейки
     */
    public void toggleFlag(int row, int column) {
        if (gameOver || win) {
            return;
        }
        Cell cell = cells[row+1][column+1];
        if (!cell.IsOpen()) {
            cell.toggleFlag();
        }
    }

    /**
     * Проверяет, не остались ли неоткрытые ячейки без мин
     * <p>Если таких нет, то игра завершается победой</p>
     */
    public void checkWin() {
        int nonMineCount = getNonMinesCount();
        if (openedCellsCount == nonMineCount) {
            gameOver = true;
            win = true;
        }
    }

    /**
     * Считает количество неоткрытых ячеек без мин
     * @return число неоткрытых ячеек без мин
     */
    private int getNonMinesCount() {
        return rows * columns - minesCount;
    }

    /**
     *
     * @return Список индексов, в которых находятся мины
     */
    public List<Integer> getMinesPositions(){
        List<Integer> positions = new ArrayList<>();
        for (int y = 1; y <= rows; y++){
            for (int x = 1; x <= columns; x++){
                if (cells[y][x].IsMine()){
                    positions.add(cells[y][x].getNumber());
                }
            }
        }
        return positions;
    }
}
