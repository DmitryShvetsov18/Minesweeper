package logic.features;

import java.util.List;

/**
 * Запись об одной сыгранной партии.
 *
 * <p>
 * Хранит все данные, необходимые для анализа завершённой игры:
 * уровень сложности, исход, затраченное время и расположение мин.
 * Используется {@link SessionStats} для накопления истории сессии
 * и {@link SessionLogger} для её сохранения на диск.
 * </p>
 *
 * <p>
 * Пример создания записи о победе на лёгком уровне:
 * </p>
 *
 * <pre>{@code
 * GameRecord record = new GameRecord(
 *     GameRecord.Difficulty.EASY,
 *     87,
 *     true,
 *     List.of(3, 15, 42)
 * );
 * System.out.println(record.getInfo());
 * }</pre>
 *
 * @author DmitriShvetsov
 * @version 1.0
 * @since 1.0
 * @see SessionStats
 * @see SessionLogger
 */
public class GameRecord {

    /**
     * Уровень сложности партии.
     *
     * <ul>
     *   <li>{@link #EASY}   — лёгкий (9×9, 10 мин)</li>
     *   <li>{@link #NORMAL} — средний (16×16, 40 мин)</li>
     *   <li>{@link #HARD}   — сложный (16×30, 99 мин)</li>
     * </ul>
     */
    public enum Difficulty {
        /** Лёгкий уровень. */
        EASY,
        /** Средний уровень. */
        NORMAL,
        /** Сложный уровень. */
        HARD
    }

    /** Уровень сложности данной партии. */
    private Difficulty difficulty;

    /** Продолжительность партии в секундах. */
    private int totalSeconds;

    /**
     * Исход партии: {@code true} — победа, {@code false} — поражение.
     */
    private boolean isWin;

    /**
     * Список индексов клеток, на которых расположены мины.
     * Индексы рассчитываются по формуле {@code row * cols + col}.
     */
    private List<Integer> minesPosition;

    /**
     * Создаёт новую запись о партии.
     *
     * @param difficulty    уровень сложности
     * @param totalSeconds  продолжительность партии в секундах (≥ 0)
     * @param isWin         {@code true}, если игрок победил
     * @param minesPosition список линейных индексов клеток с минами;
     *                      не должен быть {@code null}
     */
    public GameRecord(Difficulty difficulty, int totalSeconds, boolean isWin,
            List<Integer> minesPosition) {
        this.difficulty = difficulty;
        this.totalSeconds = totalSeconds;
        this.isWin = isWin;
        this.minesPosition = minesPosition;
    }

    /**
     * Возвращает исход партии.
     *
     * @return {@code true} — победа, {@code false} — поражение
     */
    public boolean isWin() {
        return isWin;
    }

    /**
     * Возвращает продолжительность партии в секундах.
     *
     * @return время партии в секундах (≥ 0)
     */
    public int getTotalSeconds() {
        return totalSeconds;
    }

    /**
     * Формирует текстовое описание партии.
     *
     * <p>
     * Строка содержит исход, уровень сложности, длительность
     * и позиции мин — по одному полю на строку.
     * </p>
     *
     * <p>Пример вывода:</p>
     * <pre>
     * Результат игры:    Победа
     * Сложность:         EASY
     * Длительность игры: 87
     * Положение мин[3, 15, 42]
     * </pre>
     *
     * @return многострочное описание партии
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Результат игры:    ").append((isWin ? "Победа" : "Поражение")).append("\n");
        sb.append("Сложность:         ").append(difficulty).append("\n");
        sb.append("Длительность игры: ").append(totalSeconds).append("\n");
        sb.append("Положение мин").append(minesPosition).append("\n");

        return sb.toString();
    }
}
