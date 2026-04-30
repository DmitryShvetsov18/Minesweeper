package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.mainLogic.MinesweeperGUI;

public class LevelSelectScreen {

    private final Scene scene;

    public LevelSelectScreen(Stage stage, MinesweeperGUI gui) {
        Label title = new Label("ВЫБЕРИ УРОВЕНЬ");
        title.getStyleClass().add("screen-title");

        HBox cards = new HBox(20,
                card("◎", "ЛЁГКИЙ", "9×9, 10 мин", "card-easy", () -> gui.startNewGame(9, 9, 10)),
                card("◈", "СРЕДНИЙ", "16×16, 40 мин", "card-medium",
                        () -> gui.startNewGame(16, 16, 40)),
                card("⊗", "СЛОЖНЫЙ", "16×30, 99 мин", "card-hard",
                        () -> gui.startNewGame(16, 30, 99)));
        cards.setAlignment(Pos.CENTER);

        Button back = new Button("← НАЗАД");
        back.getStyleClass().addAll("menu-btn", "btn-muted");
        back.setOnAction(e -> stage.setScene(new SplashScreen(stage, gui).getScene()));

        VBox root = new VBox(28, title, cards, back);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("dark-root");

        scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private VBox card(String icon, String name, String desc, String styleClass, Runnable onClick) {
        Label ic = new Label(icon);
        ic.getStyleClass().addAll("card-icon", styleClass);
        Label nm = new Label(name);
        nm.getStyleClass().addAll("card-name", styleClass);
        Label dsc = new Label(desc);
        dsc.getStyleClass().add("card-desc");

        VBox box = new VBox(10, ic, nm, dsc);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(150, 150);
        box.getStyleClass().addAll("level-card", styleClass);
        box.setOnMouseClicked(e -> onClick.run());
        box.setOnMouseEntered(e -> box.setScaleX(1.05));
        box.setOnMouseEntered(e -> {
            box.setScaleX(1.05);
            box.setScaleY(1.05);
        });
        box.setOnMouseExited(e -> {
            box.setScaleX(1.0);
            box.setScaleY(1.0);
        });
        return box;
    }
}
