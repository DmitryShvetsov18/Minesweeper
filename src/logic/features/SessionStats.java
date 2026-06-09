package logic.features;

import java.util.ArrayList;
import java.util.List;

public class SessionStats {

    private final List<GameRecord> listOfGames;

    public SessionStats() {
        this.listOfGames = new ArrayList<>();
    }

    public void addRecord(GameRecord record) {
        listOfGames.add(record);
    }

    public int getTotalGames() {
        return listOfGames.size();
    }

    public int getTotalWins() {
        int wins = 0;
        for (GameRecord record : listOfGames) {
            if (record.isWin())
                wins++;
        }
        return wins;
    }

    public int getTotalLoses() {
        return getTotalGames() - getTotalWins();
    }

    public double getPercentageOfWins() {
        if (getTotalGames() == 0)
            return 0;
        return (double) getTotalWins() / getTotalGames() * 100;
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Всего игр:       ").append(getTotalGames()).append("\n");
        sb.append("Побед:           ").append(getTotalWins()).append("\n");
        sb.append("Поражений:       ").append(getTotalLoses()).append("\n");
        sb.append(String.format("Процент побед:   %.1f%%\n", getPercentageOfWins()));
        sb.append("\n");

        for (int i = 0; i < listOfGames.size(); i++) {
            sb.append("--- Игра #").append(i + 1).append(" ---\n");
            sb.append(listOfGames.get(i).getInfo()).append("\n\n");
        }

        return sb.toString();
    }
}