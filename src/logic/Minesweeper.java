package logic;

import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.stream.IntStream;

public class Minesweeper {
    private final int rows;
    private final int columns;
    private final int countOfMines;
    public Cell[][] cells;

    public Minesweeper(int rows, int columns, int mines, GridPane pane) {
        this.rows = rows;
        this.columns = columns;
        this.countOfMines = mines;
        cells = MinesweeperUtils.createListOfCells(rows, columns, pane);
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public int getCountOfMines() {
        return countOfMines;
    }
    public void printCells() {
        for (int y = 1; y < rows+1; y++) {
            for (int x = 1; x < columns+1; x++) {
                if (cells[y][x].getStatus() == CellStatus.BOMB) {
                    System.out.print("B");
                }
                else {
                    System.out.print(cells[y][x].getCountOfMines());
                }
            }
            System.out.println();
        }
    }
    public void insertMines() {
        Set<Integer> minesPlaces = new HashSet<>(generateMinesPlaces());
        System.out.println(minesPlaces);
        for (int i = 1; i < rows + 1; i++) {
            for (int j = 1; j < columns + 1; j++) {
                if (minesPlaces.contains(cells[i][j].getNumber())) {
                    cells[i][j].setStatus(CellStatus.BOMB);
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

        return indexes.subList(0, this.getCountOfMines());
    }

    public void countMinesInCells() {
        for (int y = 1; y < rows + 1; y++) {
            for (int x = 1; x < columns + 1; x++) {
                int countMines = 0;
                for (int i = y-1; i < y+2; i++) {
                    for (int j = x-1; j < x+2; j++) {
                        if (cells[j][i].getStatus() == CellStatus.BOMB) {
                            countMines += 1;
                        }
                    }
                }

                cells[x][y].setCountOfMines(countMines);
            }
        }
    }
}

