package logic;

import javafx.scene.control.Button;

public class Cell extends Button {
    private static int lastNumber = 0;

    private boolean isMine = false;
    private int countNeighbours;
    private final int row;
    private final int column;
    private int number;

    public Cell(int row, int column) {
        super();
        this.row = row;
        this.column = column;
        number = lastNumber++;
    }

    public int getNumber() {
        return number;
    }

    public void setIsMine(boolean boolValue) {
        isMine = boolValue;
    }


    @Override
    public String toString() {
        return "Клетка" + "_" + number + "_" + isMine;
    }
}
