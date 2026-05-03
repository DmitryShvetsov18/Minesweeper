package logic.mainLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import gui.SplashScreen;
import javafx.scene.paint.Color;
import logic.features.GameRecord;
import logic.features.SessionStats;
import logic.features.SessionLogger;

public class MinesweeperGUI extends Application {

    private GameController controller;
    private GridPane gameGrid;
    private Stage primaryStage;
    private final SessionStats sessionStats = new SessionStats();

    // HUD
    private Label timerLabel;
    private Label flagLabel;
    private Timeline timer;
    private int secondsElapsed;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Сапер 2077");
        primaryStage.setScene(new SplashScreen(primaryStage, this).getScene());
        primaryStage.show();
    }

    @Override
    public void stop() {
        if (sessionStats.getTotalGames() > 0) {
            SessionLogger.save(sessionStats);
        }
    }

    public void startNewGame(int rows, int columns, int mines) {
        stopTimer();
        secondsElapsed = 0;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0e1a;");

        // ── Меню и HUD ──────────────────────────
        MenuBar menuBar = createMenuBar(primaryStage);
        HBox hud = buildHud(mines);

        VBox top = new VBox(menuBar, hud);
        root.setTop(top);

        // ── Контейнер для поля ──────────────────
        StackPane boardContainer = new StackPane();
        boardContainer.getStyleClass().add("game-board-container");

        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(0);
        gameGrid.setVgap(0);
        gameGrid.setPadding(javafx.geometry.Insets.EMPTY);

        boardContainer.getChildren().add(gameGrid);
        root.setCenter(boardContainer);

        // ── Контроллер ──────────────────────────
        controller = new GameController(rows, columns, mines, gameGrid, this);

        startTimer();

        Scene scene = new Scene(root, 800, 800);
        scene.setFill(Color.web("#0a0e1a"));

        java.net.URL css = getClass().getResource("/gui/style.css");
        if (css != null)
            scene.getStylesheets().add(css.toExternalForm());

        primaryStage.setScene(scene);
    }

    private HBox buildHud(int totalMines) {
        // Мины
        flagLabel = new Label("💣 " + totalMines);
        flagLabel.getStyleClass().add("hud-label");

        // Таймер
        timerLabel = new Label("⏱ 000");
        timerLabel.getStyleClass().add("hud-label");

        // КНОПКА РЕСТАРТА (Центральная)
        Button restart = new Button("↺");
        restart.getStyleClass().add("hud-restart");
        restart.setTooltip(new Tooltip("Начать заново"));
        restart.setOnAction(e -> startNewGame(controller.getRows(), controller.getColumns(),
                controller.getMines()));

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox hud = new HBox(15, flagLabel, spacer1, restart, spacer2, timerLabel);
        hud.setAlignment(Pos.CENTER);
        hud.getStyleClass().add("hud-bar");
        hud.setPadding(new Insets(10, 20, 10, 20));
        return hud;
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timerLabel.setText("⏱ " + String.format("%03d", secondsElapsed));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null)
            timer.stop();
    }

    public void updateFlagCount(int flagsPlaced, int totalMines) {
        int remaining = totalMines - flagsPlaced;
        flagLabel.setText("💣 " + remaining);
    }

    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        menuBar.setUseSystemMenuBar(false); // Важно для кастомизации

        Menu gameMenu = new Menu("ИГРА");
        gameMenu.getStyleClass().add("menu-item-custom");

        MenuItem easy = new MenuItem("Новая игра (9x9)");
        MenuItem medium = new MenuItem("Новая игра (16x16)");
        MenuItem hard = new MenuItem("Новая игра (16x30)");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem toMenu = new MenuItem("Главное меню");
        MenuItem exit = new MenuItem("Выход");

        easy.setOnAction(e -> startNewGame(9, 9, 10));
        medium.setOnAction(e -> startNewGame(16, 16, 40));
        hard.setOnAction(e -> startNewGame(16, 30, 99));
        toMenu.setOnAction(e -> goToMenu());
        exit.setOnAction(e -> stage.close());

        gameMenu.getItems().addAll(easy, medium, hard, sep, toMenu, exit);
        menuBar.getMenus().add(gameMenu);

        // Применяем класс для стилизации самого бара
        menuBar.getStyleClass().add("custom-menu-bar");

        return menuBar;
    }

    public void showWin(int seconds) {
        stopTimer();
        sessionStats.addRecord(new GameRecord(
                detectDifficulty(),
                seconds,
                true,
                controller.getMinesPositions()
        ));
        gui.GameOverPopup.showWin(primaryStage, seconds, this, controller.getRows(),
                controller.getColumns(), controller.getMines());
    }

    // Определяем сложность по размеру поля
    private GameRecord.Difficulty detectDifficulty() {
        int mines = controller.getMines();
        if (mines <= 10)  return GameRecord.Difficulty.EASY;
        if (mines <= 40)  return GameRecord.Difficulty.NORMAL;
        return GameRecord.Difficulty.HARD;
    }

    public void showLose(int seconds) {
        stopTimer();
        sessionStats.addRecord(new GameRecord(
                detectDifficulty(),
                seconds,
                false,
                controller.getMinesPositions()
        ));
        gui.GameOverPopup.showLose(primaryStage, seconds, this, controller.getRows(),
                controller.getColumns(), controller.getMines());
    }

    public void goToMenu() {
        stopTimer();
        primaryStage.setScene(new SplashScreen(primaryStage, this).getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
