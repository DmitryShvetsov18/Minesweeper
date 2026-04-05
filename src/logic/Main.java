package logic;

public class Main {
    public static void main(String[] args) {
        Minesweeper game = new Minesweeper(10, 10, 5);
        game.printCells();
    }
}
