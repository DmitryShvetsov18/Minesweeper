package logic.features;

import java.util.Arrays;
import java.util.List;

public class GameRecord {

    public enum Difficulty {EASY, NORMAL, HARD};

    private Difficulty difficulty;
    private int totalSeconds;
    private boolean isWin;
    private List<Integer> minesPosition;

    public GameRecord(Difficulty difficulty,int totalSeconds, boolean isWin, List<Integer> minesPosition) {
        this.difficulty = difficulty;
        this.totalSeconds = totalSeconds;
        this.isWin = isWin;
        this.minesPosition = minesPosition;
    }

    public boolean isWin(){
        return isWin;
    }

    public int getTotalSeconds(){
        return totalSeconds;
    }

    public String getInfo(){
        StringBuilder sb = new StringBuilder();

        sb.append("Результат игры:    ").append((isWin ? "Победа" : "Поражение")).append("\n");
        sb.append("Сложность:         ").append(difficulty).append("\n");
        sb.append("Длительность игры: ").append(totalSeconds).append("\n");
        sb.append("Положение мин").append(minesPosition).append("\n");

        return sb.toString();
    }
}

