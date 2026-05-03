package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        Button btnPlay = makeBtn("▶   ИГРАТЬ", "btn-accent");
        Button btnExit = makeBtn("✕   ВЫХОД", "btn-danger");

        btnPlay.setOnAction(e -> stage.setScene(new LevelSelectScreen(stage, gui).getScene()));
        btnExit.setOnAction(e -> stage.close());

        VBox root = new VBox(14, title, sub, btnPlay, btnExit);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("dark-root");

        scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private Button makeBtn(String text, String styleClass) {
        Button b = new Button(text);
        b.getStyleClass().addAll("menu-btn", styleClass);
        b.setPrefWidth(220);
        return b;
    }
}
