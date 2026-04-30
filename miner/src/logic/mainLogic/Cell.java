package logic.mainLogic;

public class Cell {
    private final int row;
    private final int column;
    private final int number;
    private int countOfNeighbourMines;
    private boolean isDefault;
    private boolean isOpened;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isBarrier;

    public Cell(int row, int column, int number) {
        this.row = row;
        this.column = column;
        this.number = number;
        if (number == -1) {
            isBarrier = true;
        }
        else {
            isBarrier = false;
        }
        this.countOfNeighbourMines = 0;
        this.isDefault = true;
        this.isMine = false;
        this.isOpened = false;

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
    public int getCountOfNeighbourMines() {
        return countOfNeighbourMines;
    }

    public void setIsMine(boolean bool) {
        isMine = bool;
    }
    public void setCountOfNeighbourMines(int countOfNeighbourMines) {
        this.countOfNeighbourMines = countOfNeighbourMines;
    }

    public boolean IsDefault() { return isDefault; }
    public boolean IsMine() {
        return isMine;
    }
    public boolean IsOpen() {
        return isOpened;
    }
    public boolean IsBarrier() {
        return isBarrier;
    }
    public boolean IsFlagged() {
        return isFlagged;
    }

    public void placeMine() {
        isMine = true;
    }

    public void toggleFlag() {
        if (isOpened || isBarrier) {
            return;
        }
        if (isFlagged) {
            isFlagged = false;
        } else {
            isFlagged = true;
        }
    }

    public void open() {
        if (isOpened || isBarrier || isFlagged) {
            return;
        }
        isOpened = true;
    }

    @Override
    public String toString() {
        return "Клетка" + "_" + number + "_" + isMine;
    }
}

