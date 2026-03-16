package logic;

public class Minesweeper {
    private final int rows;
    private final int columns;
    public Cell[][] cells;

    public Minesweeper(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        cells = MinesweeperUtils.createListOfCells(rows, columns);
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public void printCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }
    }
}
