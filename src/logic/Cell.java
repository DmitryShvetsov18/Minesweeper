package logic;

class Cell {
    private boolean isMine;
    private int countNeighbours;
    private final int row;
    private final int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Клетка" + row + "_" + column;
    }
}
