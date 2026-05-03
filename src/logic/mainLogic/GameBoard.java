package logic.mainLogic;


import java.util.*;
import java.util.stream.IntStream;

public class GameBoard {
    private final int rows;
    private final int columns;
    private final int minesCount;
    private final Cell[][] cells;
    private boolean gameOver;
    private boolean win;
    private int openedCellsCount;

    public GameBoard(int rows, int columns, int minesCount) {
        this.rows = rows;
        this.columns = columns;
        this.minesCount = minesCount;
        this.cells = new Cell[rows+2][columns+2];
        this.gameOver = false;
        this.win = false;
        this.openedCellsCount = 0;

        initializeCells();
        insertMines();
        calculateNeighbourMines();
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
    public void insertMines() {
        Set<Integer> minesPlaces = new HashSet<>(generateMinesPlaces());
        System.out.println(minesPlaces);
        for (int i = 1; i < rows + 1; i++) {
            for (int j = 1; j < columns + 1; j++) {
                if (minesPlaces.contains(cells[i][j].getNumber())) {
                    cells[i][j].setIsMine(true);
                }
            }
        }
    }

    public List<Integer> generateMinesPlaces() {
        int countOfCells = rows * columns;
        System.out.println(countOfCells);
        List<Integer> indexes = new ArrayList<>(
                IntStream.range(0, countOfCells).boxed().toList()
        );
        Collections.shuffle(indexes);

        return indexes.subList(0, this.getMinesCount());
    }
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

    public void openCell(int row, int column) {
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

    public void openEmptyCells(int row, int column) {
        Queue<Cell> queue = new LinkedList<>();
        HashSet<Cell> visitedCells = new HashSet<>();

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
        }
    }

    public void openAllMines() {
        for (int y = 0; y < rows + 1; y++) {
            for (int x = 0; x < columns; x++) {
                if (cells[y][x].IsMine()) {
                    cells[y][x].open();
                }
            }
        }
    }

    public void printBoard() {
        for (int y = 1; y < rows + 1; y++) {
            for (int x = 1; x < columns + 1; x++) {
                if (cells[y][x].IsMine()) {
                    System.out.print("B");
                }
                else {
                    System.out.print(cells[y][x].getCountOfNeighbourMines());
                }
            }
            System.out.println();
        }
    }

    public void toggleFlag(int row, int column) {
        if (gameOver || win) {
            return;
        }
        Cell cell = cells[row+1][column+1];
        if (!cell.IsOpen()) {
            cell.toggleFlag();
        }
    }

    public void checkWin() {
        int nonMineCount = rows * columns - minesCount;
        if (openedCellsCount == nonMineCount) {
            gameOver = true;
            win = true;
        }
    }

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
