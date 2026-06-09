package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.mainLogic.MinesweeperGUI;

/**
 * Главный экран-заставка приложения.
 * 
 * <p>
 * Первый экран, который видит пользователь при запуске,
 * а также при возврате из игры через кнопку «Меню».
 * Содержит три кнопки навигации: переход к выбору уровня,
 * просмотр статистики сессии и выход из приложения.
 * </p>
 * 
 * <p>
 * Фон масштабируется вместе с окном через привязку
 * {@code fitWidthProperty} и {@code fitHeightProperty}
 * к размерам {@link Scene}.
 * </p>
 * 
 * <p>
 * Пример использования:
 * </p>
 * 
 * <pre>{@code
 * stage.setScene(new SplashScreen(stage, gui).getScene());
 * }</pre>
 * 
 * @author Maxim
 * @version 1.0
 * @since 1.0
 * @see LevelSelectScreen
 * @see StatsPopup
 */
public class SplashScreen {

    private final Scene scene;

    /**
     * Создаёт экран-заставку и инициализирует все элементы интерфейса.
     * 
     * <p>
     * Строит вертикальный макет из заголовка, подзаголовка
     * и трёх кнопок. Фоновое изображение {@code fon1.png} загружается
     * из ресурсов и привязывается к размерам сцены.
     * </p>
     * 
     * @param stage основной {@link Stage} приложения, в который будет установлена
     *              сцена
     * @param gui   экземпляр {@link MinesweeperGUI} для навигации и доступа к
     *              статистике
     */
    public SplashScreen(Stage stage, MinesweeperGUI gui) {
        Label title = new Label("САПЁР");
        title.getStyleClass().add("splash-title");

        Button btnPlay = makeBtn("▶   ИГРАТЬ", "btn-accent");
        Button btnStats = makeBtn("📊   СТАТИСТИКА", "btn-muted");
        Button btnExit = makeBtn("✕   ВЫХОД", "btn-danger");

        btnPlay.setOnAction(e -> stage.setScene(new LevelSelectScreen(stage, gui).getScene()));
        btnStats.setOnAction(e -> StatsPopup.show(stage, gui.getSessionStats()));
        btnExit.setOnAction(e -> stage.close());

        VBox root = new VBox(14, title, btnPlay, btnStats, btnExit);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/images/fon1.png")));
        bg.setPreserveRatio(false);

        StackPane wrapper = new StackPane(bg, root);
        scene = new Scene(wrapper, 900, 700);

        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());

        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
    }

    /**
     * Возвращает готовую сцену экрана-заставки.
     * 
     * <p>
     * Используется для установки сцены в {@link Stage}:
     * {@code stage.setScene(splashScreen.getScene())}.
     * </p>
     * 
     * @return {@link Scene} с разрешением 900×700, готовая к отображению
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Создаёт стилизованную кнопку главного меню.
     * 
     * <p>
     * Все кнопки имеют общий класс {@code menu-btn} и фиксированную
     * ширину 260px. Цветовое оформление определяется дополнительным
     * CSS-классом {@code styleClass}.
     * </p>
     * 
     * @param text       текст кнопки
     * @param styleClass дополнительный CSS-класс: {@code btn-accent},
     *                   {@code btn-muted} или {@code btn-danger}
     * @return готовая кнопка с заданным стилем
     */
    private Button makeBtn(String text, String styleClass) {
        Button b = new Button(text);
        b.getStyleClass().addAll("menu-btn", styleClass);
        b.setPrefWidth(260);
        return b;
    }
}