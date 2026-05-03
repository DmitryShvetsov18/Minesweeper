package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.mainLogic.MinesweeperGUI;
import javafx.scene.paint.Color;

public class GameOverPopup {

        public static void showLose(Stage owner, int seconds, MinesweeperGUI gui, int rows,
                        int cols, int mines) {
                show(owner, seconds, gui, rows, cols, mines, false);
        }

        public static void showWin(Stage owner, int seconds, MinesweeperGUI gui, int rows, int cols,
                        int mines) {
                show(owner, seconds, gui, rows, cols, mines, true);
        }

        private static void show(Stage owner, int seconds, MinesweeperGUI gui, int rows, int cols,
                        int mines, boolean win) {
                // Используем Stage вместо Popup для надёжности
                Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.initOwner(owner);

                // ── содержимое ──────────────────────────────
                Label title = new Label(win ? "ПОБЕДА!" : "ВЗРЫВ");
                title.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 42px;"
                                + "-fx-font-weight: bold; -fx-text-fill: "
                                + (win ? "#4ade80" : "#e24b4a") + ";");

                Label sub = new Label(win ? "// все мины обезврежены" : "// вы наступили на мину");
                sub.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;"
                                + "-fx-text-fill: #64748b;");

                String timeStr = seconds >= 60 ? (seconds / 60) + "м " + (seconds % 60) + "с"
                                : seconds + "с";
                Label time = new Label("⏱  Время: " + timeStr);
                time.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 16px;"
                                + "-fx-text-fill: #e2e8f0;");

                Button btnRetry = popupBtn(win ? "▶   ЕЩЁ РАЗ" : "↺   СНОВА",
                                win ? "#4ade80" : "#e24b4a");
                Button btnMenu = popupBtn("⌂   МЕНЮ", "#64748b");

                btnRetry.setOnAction(e -> {
                        popupStage.close();
                        gui.startNewGame(rows, cols, mines);
                });
                btnMenu.setOnAction(e -> {
                        popupStage.close();
                        gui.goToMenu();
                });

                VBox box = new VBox(16, title, sub, time, btnRetry, btnMenu);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(36, 48, 36, 48));
                box.setStyle("-fx-background-color: #111827;" + "-fx-border-color: "
                                + (win ? "#4ade80" : "#e24b4a") + ";" + "-fx-border-width: 2px;"
                                + "-fx-border-radius: 12px;" + "-fx-background-radius: 12px;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 20, 0, 0, 0);");

                // Корневой узел с прозрачным фоном
                StackPane root = new StackPane(box);
                root.setStyle("-fx-background-color: transparent;");
                root.setAlignment(Pos.CENTER);

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT); // КЛЮЧЕВОЙ МОМЕНТ!

                // Загружаем стили, если нужно
                scene.getStylesheets().add(
                                GameOverPopup.class.getResource("/gui/style.css").toExternalForm());

                popupStage.setScene(scene);
                popupStage.sizeToScene();

                // Центрируем относительно владельца
                popupStage.setOnShown(e -> {
                        popupStage.setX(owner.getX()
                                        + (owner.getWidth() - popupStage.getWidth()) / 2);
                        popupStage.setY(owner.getY()
                                        + (owner.getHeight() - popupStage.getHeight()) / 2);
                });

                popupStage.show();
        }

        private static Button popupBtn(String text, String color) {
                Button b = new Button(text);
                b.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;"
                                + "-fx-font-weight: bold; -fx-background-color: transparent;"
                                + "-fx-border-color: " + color + "; -fx-border-radius: 4px;"
                                + "-fx-background-radius: 4px; -fx-text-fill: " + color + ";"
                                + "-fx-padding: 10 20 10 20; -fx-cursor: hand;");
                b.setPrefWidth(220);
                b.setOnMouseEntered(e -> b.setOpacity(0.75));
                b.setOnMouseExited(e -> b.setOpacity(1.0));
                return b;
        }
}
