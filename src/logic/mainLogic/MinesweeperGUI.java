package logic.mainLogic;

import gui.SplashScreen;
import gui.StatsPopup;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.features.GameRecord;
import logic.features.SessionLogger;
import logic.features.SessionStats;

public class MinesweeperGUI extends Application {

    private GameController controller;
    private GridPane gameGrid;
    private Stage primaryStage;
    private final SessionStats sessionStats = new SessionStats();

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
        root.setStyle("-fx-background-color: transparent;");

        HBox hud = buildHud(mines);
        root.setTop(hud);

        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(0);
        gameGrid.setVgap(0);
        gameGrid.setPadding(Insets.EMPTY);

        // FIX 1: оборачиваем GridPane в StackPane — он центрирует сетку без растяжения.
        // ScrollPane получает StackPane с fitToWidth/Height=true, что заставляет
        // внутренний StackPane заполнить viewport, а он уже центрирует GridPane.
        StackPane gridWrapper = new StackPane(gameGrid);
        gridWrapper.setAlignment(Pos.CENTER);
        gridWrapper.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(gridWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        StackPane boardContainer = new StackPane(scrollPane);
        boardContainer.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        boardContainer.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(boardContainer, Pos.CENTER);

        root.setCenter(boardContainer);

        controller = new GameController(rows, columns, mines, gameGrid, this);

        startTimer();

        // FIX 3: адаптивный фон — ImageView привязывается к размеру сцены
        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/images/fon3.png")));
        bg.setPreserveRatio(false);

        StackPane gameRoot = new StackPane(bg, root);

        Scene scene = new Scene(gameRoot, 1000, 750);
        scene.setFill(Color.web("#060a12"));

        // FIX 3: фон растягивается вместе с окном
        bg.fitWidthProperty().bind(scene.widthProperty());
        bg.fitHeightProperty().bind(scene.heightProperty());

        java.net.URL css = getClass().getResource("/gui/style.css");
        if (css != null)
            scene.getStylesheets().add(css.toExternalForm());

        primaryStage.setScene(scene);
    }

    private HBox buildHud(int totalMines) {
        // ── Левая часть ──
        flagLabel = new Label("\uD83D\uDCA3  " + totalMines);
        flagLabel.getStyleClass().add("hud-label");

        HBox leftGroup = new HBox(flagLabel);
        leftGroup.setAlignment(Pos.CENTER_LEFT);
        leftGroup.setMinWidth(200);
        leftGroup.setPrefWidth(200);

        // ── Центр: кнопка рестарта ──
        Button restart = new Button("\u21BA");
        restart.getStyleClass().add("hud-restart");
        restart.setTooltip(new Tooltip("Начать заново"));
        restart.setOnAction(e -> startNewGame(controller.getRows(), controller.getColumns(),
                controller.getMines()));

        // ── Правая часть ──
        timerLabel = new Label("\u23F1  000");
        timerLabel.getStyleClass().add("hud-label");

        // FIX 2: шестерёнка белая, центрирована
        MenuButton settingsBtn = new MenuButton("\u2699");
        settingsBtn.getStyleClass().add("hud-settings-btn");
        settingsBtn.setStyle(
            "-fx-font-size: 18px; -fx-text-fill: #ffffff;"
        );
        settingsBtn.setPrefWidth(44);
        settingsBtn.setMinWidth(44);
        settingsBtn.setMaxWidth(44);
        settingsBtn.setPrefHeight(44);
        settingsBtn.setMinHeight(44);
        settingsBtn.setMaxHeight(44);

        MenuItem easy   = new MenuItem("Новая игра  9\u00D79");
        MenuItem medium = new MenuItem("Новая игра  16\u00D716");
        MenuItem hard   = new MenuItem("Новая игра  16\u00D730");
        SeparatorMenuItem sep1  = new SeparatorMenuItem();
        MenuItem stats  = new MenuItem("Статистика сессии");
        SeparatorMenuItem sep2  = new SeparatorMenuItem();
        MenuItem toMenu = new MenuItem("Главное меню");
        MenuItem exit   = new MenuItem("Выход");

        easy.setOnAction(e -> startNewGame(9, 9, 10));
        medium.setOnAction(e -> startNewGame(16, 16, 40));
        hard.setOnAction(e -> startNewGame(16, 30, 99));
        stats.setOnAction(e -> StatsPopup.show(primaryStage, sessionStats));
        toMenu.setOnAction(e -> goToMenu());
        exit.setOnAction(e -> primaryStage.close());

        settingsBtn.getItems().addAll(easy, medium, hard, sep1, stats, sep2, toMenu, exit);

        HBox rightGroup = new HBox(8, timerLabel, settingsBtn);
        rightGroup.setAlignment(Pos.CENTER_RIGHT);
        rightGroup.setMinWidth(200);
        rightGroup.setPrefWidth(200);

        // ── Спейсеры ──
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox hud = new HBox(0, leftGroup, spacer1, restart, spacer2, rightGroup);
        hud.setAlignment(Pos.CENTER);
        hud.getStyleClass().add("hud-bar");
        hud.setPadding(new Insets(12, 20, 12, 20));

        return hud;
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timerLabel.setText("\u23F1  " + String.format("%03d", secondsElapsed));
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
        flagLabel.setText("\uD83D\uDCA3  " + remaining);
    }

    public void showWin(int seconds) {
        stopTimer();
        sessionStats.addRecord(
                new GameRecord(detectDifficulty(), seconds, true, controller.getMinesPositions()));
        gui.GameOverPopup.showWin(primaryStage, seconds, this, controller.getRows(),
                controller.getColumns(), controller.getMines());
    }

    private GameRecord.Difficulty detectDifficulty() {
        int mines = controller.getMines();
        if (mines <= 10)
            return GameRecord.Difficulty.EASY;
        if (mines <= 40)
            return GameRecord.Difficulty.NORMAL;
        return GameRecord.Difficulty.HARD;
    }

    public void showLose(int seconds) {
        stopTimer();
        sessionStats.addRecord(
                new GameRecord(detectDifficulty(), seconds, false, controller.getMinesPositions()));
        gui.GameOverPopup.showLose(primaryStage, seconds, this, controller.getRows(),
                controller.getColumns(), controller.getMines());
    }

    public SessionStats getSessionStats() {
        return sessionStats;
    }

    public void goToMenu() {
        stopTimer();
        primaryStage.setScene(new SplashScreen(primaryStage, this).getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
