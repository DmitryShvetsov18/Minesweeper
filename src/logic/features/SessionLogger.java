package logic.features;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилитный класс для сохранения статистики сессии в файл.
 *
 * <p>
 * При каждом вызове {@link #save} создаёт новый текстовый файл в папке
 * {@code sessions/} (относительно рабочей директории приложения).
 * Имя файла формируется по текущей дате и времени, что гарантирует
 * уникальность при многократных сохранениях за одну сессию.
 * </p>
 *
 * <p>
 * Если папка {@code sessions/} отсутствует, она создаётся автоматически.
 * При возникновении ошибок ввода-вывода класс не бросает исключений —
 * сообщение об ошибке выводится в {@code System.err}.
 * </p>
 *
 * <p>
 * Пример использования:
 * </p>
 *
 * <pre>{@code
 * SessionStats stats = new SessionStats();
 * stats.addRecord(record);
 * SessionLogger.save(stats); // → sessions/session_2024-05-03_14-22-07.txt
 * }</pre>
 *
 * @author DmitriShvetsov
 * @version 1.0
 * @since 1.0
 * @see SessionStats
 * @see GameRecord
 */
public class SessionLogger {

    /**
     * Папка, в которую сохраняются файлы сессий.
     * Путь относительный — разрешается от рабочей директории JVM.
     */
    private static final Path SAVE_DIR = Path.of("sessions");

    /**
     * Запрещает создание экземпляров утилитного класса.
     */
    private SessionLogger() {
    }

    /**
     * Сохраняет статистику сессии в текстовый файл.
     *
     * <p>
     * Алгоритм работы:
     * <ol>
     *   <li>При необходимости создаёт директорию {@code sessions/}.</li>
     *   <li>Формирует имя файла вида {@code session_yyyy-MM-dd_HH-mm-ss.txt}.</li>
     *   <li>Записывает результат {@link SessionStats#getInfo()} в созданный файл.</li>
     * </ol>
     * </p>
     *
     * <p>
     * Метод <em>не</em> бросает исключений: все ошибки ввода-вывода перехватываются
     * и выводятся в {@code System.err} с префиксом {@code [SessionLogger]}.
     * </p>
     *
     * @param stats статистика текущей сессии; не должна быть {@code null}
     */
    public static void save(SessionStats stats) {
        try {
            if (!Files.exists(SAVE_DIR)) {
                Files.createDirectories(SAVE_DIR);
            }
        } catch (IOException e) {
            System.err.println("[SessionLogger] Не удалось создать папку: " + e.getMessage());
            return;
        }

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path file = SAVE_DIR.resolve("session_" + timestamp + ".txt");

        // Запись
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(stats.getInfo());
        } catch (IOException e) {
            System.err.println("[SessionLogger] Не удалось записать файл: " + e.getMessage());
        }
    }
}
