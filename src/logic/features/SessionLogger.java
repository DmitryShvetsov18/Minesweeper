package logic.features;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionLogger {

    private static final Path SAVE_DIR = Path.of("sessions");

    public static void save(SessionStats stats) {
        // Создаём папку sessions если её нет
        try {
            if (!Files.exists(SAVE_DIR)) {
                Files.createDirectories(SAVE_DIR);
            }
        } catch (IOException e) {
            System.err.println("[SessionLogger] Не удалось создать папку: " + e.getMessage());
            return;
        }

        // Имя файла — дата и время запуска сессии
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