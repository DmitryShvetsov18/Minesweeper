package logic;

import javafx.scene.control.Button;

public class Cell extends Button {

    private CellStatus status;
    private int countOfMines;
    private final int row;
    private final int column;
    private int number;

    public Cell(int row, int column, int number) {
        super();
        this.row = row;
        this.column = column;
        this.number = number;
        this.status = CellStatus.DEFAULT;
        this.countOfMines = -1;
    }
    public Cell(int row, int column) {
        super();
        this.row = row;
        this.column = column;
        this.number = -1;
    }

    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public int getNumber() {
        return number;
    }
    public CellStatus getStatus() {
        return status;
    }
    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public void setCountOfMines(int countOfMines) {
        this.countOfMines = countOfMines;
    }
    public int getCountOfMines() {
        return this.countOfMines;
    }

    @Override
    public String toString() {
        return "Клетка" + "_" + number + "_" + status;
    }
}

enum CellStatus {
    DEFAULT,
    BOMB,
    FLAG
}