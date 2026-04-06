package logic;

import javafx.scene.control.Button;

public class Cell extends Button {

    private boolean isMine = false;
    private int countOfMines;
    private final int row;
    private final int column;
    private int number;

    public Cell(int row, int column, int number) {
        super();
        this.row = row;
        this.column = column;
        this.number = number;
    }
    public Cell(int row, int column) {
        super();
        this.row = row;
        this.column = column;
        this.number = -1;
    }

    public int getNumber() {
        return number;
    }

    public void setIsMine(boolean boolValue) {
        isMine = boolValue;
    }
    public boolean IsMine() {
        return isMine;
    }

    public void setCountOfMines(int countOfMines) {
        this.countOfMines = countOfMines;
    }
    public int getCountOfMines() {
        return this.countOfMines;
    }

    @Override
    public String toString() {
        return "Клетка" + "_" + number + "_" + isMine;
    }
}
