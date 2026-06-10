package logic.mainLogic;

/**
 * Представляет клетку игрового поля сапёра.
 * <p>
 * Клетка хранит информацию о своем положении на поле,
 * наличии мины, количестве соседних мин, а также
 * состоянии (открыта, помечена флагом и т.д.).
 * </p>
 * <p>
 * Для упрощения обработки границ поля используются
 * специальные барьерные клетки. Такие клетки не участвуют
 * в игровом процессе и имеют номер     {@code -1}.
 * </p>
 */
public class Cell {
    private final int row;
    private final int column;
    private final int number;
    private int countOfNeighbourMines;
    private boolean isOpened;
    private boolean isMine;
    private boolean isFlagged;
    private final boolean isBarrier;

    /**
     * Создает новую клетку игрового поля.
     *
     * @param row номер строки клетки
     * @param column номер столбца клетки
     * @param number уникальный номер клетки.
     *               Значение {@code -1} используется для барьерных клеток.
     */
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

