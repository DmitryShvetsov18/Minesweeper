package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.features.SessionStats;

/**
 * Всплывающее окно статистики текущей сессии.
 * <p>
 * Отображает сводку по сыгранным партиям: общее количество игр, победы, поражения и процент побед.
 * Окно модальное, прозрачного стиля, центрируется относительно родительского {@link Stage}.
 * </p>
 */
public class StatsPopup {

        /** Запрещает создание экземпляров утилитного класса. */
        private StatsPopup() {}

        /**
         * Показывает окно статистики сессии.
         *
         * @param owner родительское окно игры
         * @param stats объект с данными статистики текущей сессии
         */
        public static void show(Stage owner, SessionStats stats) {
                Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.initOwner(owner);

                Label title = new Label("СТАТИСТИКА  СЕССИИ");
                title.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 20px;"
                                + "-fx-font-weight: bold;" + "-fx-text-fill: #00e5ff;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,229,255,0.8), 15, 0.3, 0, 0);");

                Region divider = new Region();
                divider.setPrefHeight(1);
                divider.setPrefWidth(320);
                divider.setStyle("-fx-background-color: #1e3a5f;");

                int total = stats.getTotalGames();
                int wins = stats.getTotalWins();
                int loses = stats.getTotalLoses();
                String pct = total > 0 ? String.format("%.1f%%", (wins * 100.0 / total)) : "0.0%";

                VBox statsBox = new VBox(14, statRow("Всего игр", String.valueOf(total), "#e2e8f0"),
                                statRow("Победы", String.valueOf(wins), "#4ade80"),
                                statRow("Поражения", String.valueOf(loses), "#e24b4a"),
                                statRow("Процент побед", pct, "#00e5ff"));
                statsBox.setAlignment(Pos.CENTER_LEFT);
                statsBox.setPadding(new Insets(10, 0, 10, 0));

                Button btnClose = new Button("✕   ЗАКРЫТЬ");
                btnClose.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 14px;"
                                + "-fx-font-weight: bold;" + "-fx-background-color: transparent;"
                                + "-fx-border-color: #1e3a5f;" + "-fx-border-radius: 6;"
                                + "-fx-background-radius: 6;" + "-fx-text-fill: #7ab0d8;"
                                + "-fx-padding: 10 20 10 20;" + "-fx-cursor: hand;"
                                + "-fx-pref-width: 240;");
                btnClose.setOnMouseEntered(e -> {
                        btnClose.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 14px;"
                                        + "-fx-font-weight: bold;"
                                        + "-fx-background-color: rgba(0,229,255,0.10);"
                                        + "-fx-border-color: #00e5ff;" + "-fx-border-radius: 6;"
                                        + "-fx-background-radius: 6;" + "-fx-text-fill: #00e5ff;"
                                        + "-fx-padding: 10 20 10 20;" + "-fx-cursor: hand;"
                                        + "-fx-pref-width: 240;");
                });
                btnClose.setOnMouseExited(e -> {
                        btnClose.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 14px;"
                                        + "-fx-font-weight: bold;"
                                        + "-fx-background-color: transparent;"
                                        + "-fx-border-color: #1e3a5f;" + "-fx-border-radius: 6;"
                                        + "-fx-background-radius: 6;" + "-fx-text-fill: #7ab0d8;"
                                        + "-fx-padding: 10 20 10 20;" + "-fx-cursor: hand;"
                                        + "-fx-pref-width: 240;");
                });
                btnClose.setOnAction(e -> popupStage.close());

                VBox box = new VBox(16, title, divider, statsBox, btnClose);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(36, 40, 36, 40));
                box.setStyle("-fx-background-color: #0d1117;" + "-fx-border-color: #1e3a5f;"
                                + "-fx-border-width: 2;" + "-fx-border-radius: 14;"
                                + "-fx-background-radius: 14;"
                                + "-fx-effect: dropshadow(gaussian, rgba(0,229,255,0.2), 30, 0, 0, 0);");

                StackPane root = new StackPane(box);
                root.setStyle("-fx-background-color: transparent;");

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().add(
                                StatsPopup.class.getResource("/gui/style.css").toExternalForm());

                popupStage.setScene(scene);
                popupStage.sizeToScene();
                popupStage.setOnShown(e -> {
                        popupStage.setX(owner.getX()
                                        + (owner.getWidth() - popupStage.getWidth()) / 2);
                        popupStage.setY(owner.getY()
                                        + (owner.getHeight() - popupStage.getHeight()) / 2);
                });

                popupStage.show();
        }

        /**
         * Создаёт строку статистики: название слева, значение справа.
         *
         * @param label название показателя
         * @param value значение показателя
         * @param valueColor hex-цвет значения
         * @return {@link HBox} со стилизованной строкой
         */
        private static HBox statRow(String label, String value, String valueColor) {
                Label lbl = new Label(label);
                lbl.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 14px;"
                                + "-fx-text-fill: #607090;");
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                Label val = new Label(value);
                val.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 18px;"
                                + "-fx-font-weight: bold;" + "-fx-text-fill: " + valueColor + ";");
                HBox row = new HBox(lbl, spacer, val);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPrefWidth(320);
                return row;
        }
}
