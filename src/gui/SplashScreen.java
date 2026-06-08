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
 * <p>
 * Отображается при запуске и при возврате из игры в меню. Содержит кнопки перехода к выбору уровня,
 * просмотру статистики и выходу из приложения.
 * </p>
 */
public class SplashScreen {

    private final Scene scene;

    /**
     * Создаёт экран-заставку.
     *
     * @param stage основной {@link Stage} приложения
     * @param gui экземпляр главного приложения для навигации и статистики
     */
    public SplashScreen(Stage stage, MinesweeperGUI gui) {
        Label title = new Label("САПЁР");
        title.getStyleClass().add("splash-title");

        Label sub = new Label("Найди все мины. Или умри.");
        sub.getStyleClass().add("splash-sub");

        Button btnPlay = makeBtn("▶   ИГРАТЬ", "btn-accent");
        Button btnStats = makeBtn("📊   СТАТИСТИКА", "btn-muted");
        Button btnExit = makeBtn("✕   ВЫХОД", "btn-danger");

        btnPlay.setOnAction(e -> stage.setScene(new LevelSelectScreen(stage, gui).getScene()));
        btnStats.setOnAction(e -> StatsPopup.show(stage, gui.getSessionStats()));
        btnExit.setOnAction(e -> stage.close());

        VBox root = new VBox(14, title, sub, btnPlay, btnStats, btnExit);
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
     * Возвращает сцену главного экрана.
     *
     * @return {@link Scene} для установки в {@link Stage}
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Создаёт стилизованную кнопку главного меню.
     *
     * @param text текст кнопки
     * @param styleClass дополнительный CSS-класс для цветового оформления
     * @return готовая кнопка с фиксированной шириной
     */
    private Button makeBtn(String text, String styleClass) {
        Button b = new Button(text);
        b.getStyleClass().addAll("menu-btn", styleClass);
        b.setPrefWidth(260);
        return b;
    }
}
