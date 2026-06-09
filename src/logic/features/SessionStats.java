package logic.features;

import java.util.ArrayList;
import java.util.List;

/**
 * Статистика игровой сессии.
 *
 * <p>
 * Накапливает записи {@link GameRecord} в рамках одного запуска приложения
 * и предоставляет агрегированные показатели: общее число партий, количество
 * побед и поражений, процент побед.
 * Для сохранения собранной статистики на диск используйте {@link SessionLogger}.
 * </p>
 *
 * <p>
 * Пример использования:
 * </p>
 *
 * <pre>{@code
 * SessionStats stats = new SessionStats();
 * stats.addRecord(new GameRecord(Difficulty.EASY, 45, true, positions));
 * stats.addRecord(new GameRecord(Difficulty.NORMAL, 120, false, positions));
 *
 * System.out.println("Побед: " + stats.getTotalWins());          // 1
 * System.out.println("Процент: " + stats.getPercentageOfWins()); // 50.0
 * SessionLogger.save(stats);
 * }</pre>
 *
 * @author DmitriShvetsov
 * @version 1.0
 * @since 1.0
 * @see GameRecord
 * @see SessionLogger
 */
public class SessionStats {

    /**
     * Упорядоченный список записей о партиях текущей сессии.
     * Порядок соответствует порядку вызовов {@link #addRecord}.
     */
    private final List<GameRecord> listOfGames;

    /**
     * Создаёт пустую статистику сессии.
     * Список партий изначально пуст.
     */
    public SessionStats() {
        this.listOfGames = new ArrayList<>();
    }

    /**
     * Добавляет запись о завершённой партии.
     *
     * @param record запись о партии; не должна быть {@code null}
     */
    public void addRecord(GameRecord record) {
        listOfGames.add(record);
    }

    /**
     * Возвращает суммарное число партий в сессии.
     *
     * @return количество сыгранных партий (≥ 0)
     */
    public int getTotalGames() {
        return listOfGames.size();
    }

    /**
     * Возвращает число побед в текущей сессии.
     *
     * @return количество партий, завершившихся победой (≥ 0)
     * @see GameRecord#isWin()
     */
    public int getTotalWins() {
        int wins = 0;
        for (GameRecord record : listOfGames) {
            if (record.isWin())
                wins++;
        }
        return wins;
    }

    /**
     * Возвращает число поражений в текущей сессии.
     *
     * <p>Вычисляется как разность {@link #getTotalGames()} − {@link #getTotalWins()}.</p>
     *
     * @return количество партий, завершившихся поражением (≥ 0)
     */
    public int getTotalLoses() {
        return getTotalGames() - getTotalWins();
    }

    /**
     * Возвращает процент победных партий в сессии.
     *
     * <p>
     * Если ни одной партии ещё не сыграно, возвращает {@code 0.0},
     * чтобы избежать деления на ноль.
     * </p>
     *
     * @return процент побед от 0.0 до 100.0 включительно
     */
    public double getPercentageOfWins() {
        if (getTotalGames() == 0)
            return 0;
        return (double) getTotalWins() / getTotalGames() * 100;
    }

    /**
     * Формирует полный текстовый отчёт о сессии.
     *
     * <p>
     * Отчёт включает сводную строку (итого, победы, поражения, процент)
     * и подробное описание каждой партии через {@link GameRecord#getInfo()}.
     * </p>
     *
     * <p>Пример вывода для двух партий:</p>
     * <pre>
     * Всего игр:       2
     * Побед:           1
     * Поражений:       1
     * Процент побед:   50.0%
     *
     * --- Игра #1 ---
     * Результат игры:    Победа
     * ...
     *
     * --- Игра #2 ---
     * Результат игры:    Поражение
     * ...
     * </pre>
     *
     * @return многострочный отчёт о сессии
     */
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
