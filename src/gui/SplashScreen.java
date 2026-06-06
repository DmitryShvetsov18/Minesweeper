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

public class SplashScreen {

    private final Scene scene;

    public SplashScreen(Stage stage, MinesweeperGUI gui) {
        Label title = new Label("САПЁР");
        title.getStyleClass().add("splash-title");

        Label sub = new Label("Найди все мины. Или умри.");
        sub.getStyleClass().add("splash-sub");

        Button btnPlay  = makeBtn("▶   ИГРАТЬ", "btn-accent");
        Button btnStats = makeBtn("📊   СТАТИСТИКА", "btn-muted");
        Button btnExit  = makeBtn("✕   ВЫХОД", "btn-danger");

        btnPlay.setOnAction(e -> stage.setScene(new LevelSelectScreen(stage, gui).getScene()));
        btnStats.setOnAction(e -> StatsPopup.show(stage, gui.getSessionStats()));
        btnExit.setOnAction(e -> stage.close());

        VBox root = new VBox(14, title, sub, btnPlay, btnStats, btnExit);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        // FIX 3: фон масштабируется вместе с окном
        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/images/fon1.png")));
        bg.setPreserveRatio(false);

        StackPane wrapper = new StackPane(bg, root);
        scene = new Scene(wrapper, 900, 700);

        // Привязываем размер фона к размеру сцены
        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());

        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private Button makeBtn(String text, String styleClass) {
        Button b = new Button(text);
        b.getStyleClass().addAll("menu-btn", styleClass);
        b.setPrefWidth(260);
        return b;
    }
}
