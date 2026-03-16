package logic;


public class MinesweeperUtils {
    public static Cell[][] createListOfCells(int rows, int columns) {
        Cell[][] result = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = new Cell(i, j);
            }
        }
        return result;
    }
}
