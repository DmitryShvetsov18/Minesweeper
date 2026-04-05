package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Minesweeper {
    private final int rows;
    private final int columns;
    private final int countOfMines;
    public Cell[][] cells;

    public Minesweeper(int rows, int columns, int mines) {
        this.rows = rows;
        this.columns = columns;
        this.countOfMines = mines;
        cells = MinesweeperUtils.createListOfCells(rows, columns);
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void insertMines() {
        List<Integer> minesPlaces = generateMinesPlaces();
        System.out.println(minesPlaces);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minesPlaces.contains(cells[i][j].getNumber())) {
                    cells[i][j].setIsMine(true);
                }
            }
        }
    }

    public List<Integer> generateMinesPlaces() {
        int countOfCells = this.getRows() * this.getColumns();
        List<Integer> indexes = new ArrayList<>(
            IntStream.range(0, countOfCells).boxed().toList()
        );
        Collections.shuffle(indexes);

        return indexes.subList(0, this.getCountOfMines());
    }
}
