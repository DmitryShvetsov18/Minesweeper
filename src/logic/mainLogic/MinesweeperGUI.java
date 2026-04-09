package logic.mainLogic;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.mainLogic.GameController;

public class MinesweeperGUI extends Application {

    private GameController controller;
    private GridPane gameGrid;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);

        MenuBar menuBar = createMenuBar(primaryStage);
        root.setTop(menuBar);
        root.setCenter(gameGrid);

        startNewGame(10, 10, 15);

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Сапер 2077");
        primaryStage.show();
    }

    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Игра");

        MenuItem newGameEasy = new MenuItem("Новая игра (9x9, 10 мин)");
        newGameEasy.setOnAction(e -> startNewGame(9, 9, 10));

        MenuItem newGameMedium = new MenuItem("Новая игра (16x16, 40 мин)");
        newGameMedium.setOnAction(e -> startNewGame(16, 16, 40));

        MenuItem newGameHard = new MenuItem("Новая игра (16x30, 99 мин)");
        newGameHard.setOnAction(e -> startNewGame(16, 30, 99));

        MenuItem exit = new MenuItem("Выход");
        exit.setOnAction(e -> stage.close());

        gameMenu.getItems().addAll(newGameEasy, newGameMedium, newGameHard, exit);
        menuBar.getMenus().add(gameMenu);

        return menuBar;
    }

    private void startNewGame(int rows, int columns, int mines) {
        controller = new GameController(rows, columns, mines, gameGrid);
        controller.printBoard();
    }

    public static void main(String[] args) {
        launch(args);
    }
}