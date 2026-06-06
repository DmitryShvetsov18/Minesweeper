package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.mainLogic.MinesweeperGUI;

public class LevelSelectScreen {

    private final Scene scene;

    public LevelSelectScreen(Stage stage, MinesweeperGUI gui) {
        Label title = new Label("ВЫБЕРИ  УРОВЕНЬ");
        title.getStyleClass().add("screen-title");
        title.setPadding(new javafx.geometry.Insets(0, 0, 2, 0));

        // Декоративная линия под заголовком
        javafx.scene.shape.Line lineLeft = new javafx.scene.shape.Line(0, 0, 120, 0);
        lineLeft.setStroke(javafx.scene.paint.Color.web("#00e5ff"));
        lineLeft.setStrokeWidth(1.5);

        javafx.scene.shape.Line lineRight = new javafx.scene.shape.Line(0, 0, 120, 0);
        lineRight.setStroke(javafx.scene.paint.Color.web("#00e5ff"));
        lineRight.setStrokeWidth(1.5);

        javafx.scene.shape.Rectangle lineDot = new javafx.scene.shape.Rectangle(8, 8);
        lineDot.setFill(javafx.scene.paint.Color.web("#00e5ff"));
        lineDot.setEffect(
                new javafx.scene.effect.DropShadow(10, javafx.scene.paint.Color.web("#00e5ff")));
        lineDot.setRotate(45);

        HBox divider = new HBox(10, lineLeft, lineDot, lineRight);
        divider.setAlignment(Pos.CENTER);

        // FIX 4: правильные размеры — 9×9 10 мин, 16×16 40 мин, 16×30 99 мин
        HBox cards = new HBox(24,
                card("easy.png", "ЛЁГКИЙ",   "9×9  ·  10 мин",   "card-easy",
                        () -> gui.startNewGame(9, 9, 10)),
                card("mid.png",  "СРЕДНИЙ",  "16×16  ·  40 мин", "card-medium",
                        () -> gui.startNewGame(16, 16, 40)),
                card("high.png", "СЛОЖНЫЙ",  "16×30  ·  99 мин", "card-hard",
                        () -> gui.startNewGame(16, 30, 99)));
        cards.setAlignment(Pos.CENTER);

        Button back = new Button("←   НАЗАД");
        back.getStyleClass().addAll("menu-btn", "btn-back");
        back.setOnAction(e -> stage.setScene(new SplashScreen(stage, gui).getScene()));

        VBox root = new VBox(0, title, divider, cards, back);
        VBox.setMargin(divider, new javafx.geometry.Insets(2, 0, 30, 0));
        VBox.setMargin(back, new javafx.geometry.Insets(30, 0, 0, 0));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: transparent; -fx-padding: 40;");

        // FIX 3: фон масштабируется вместе с окном
        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/images/fon2.png")));
        bg.setPreserveRatio(false);

        StackPane wrapper = new StackPane(bg, root);
        scene = new Scene(wrapper, 900, 700);

        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());

        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
    }

    public Scene getScene() {
        return scene;
    }

    private VBox card(String imageName, String name, String desc, String styleClass,
            Runnable onClick) {
        ImageView img =
                new ImageView(new Image(getClass().getResourceAsStream("/images/" + imageName)));
        img.setFitWidth(100);
        img.setFitHeight(100);
        img.setPreserveRatio(true);

        Label nm = new Label(name);
        nm.getStyleClass().addAll("card-name", styleClass);
        Label dsc = new Label(desc);
        dsc.getStyleClass().add("card-desc");

        VBox box = new VBox(12, img, nm, dsc);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(200, 220);
        box.setMinWidth(200);
        box.getStyleClass().addAll("level-card", styleClass);
        box.setOnMouseClicked(e -> onClick.run());
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
