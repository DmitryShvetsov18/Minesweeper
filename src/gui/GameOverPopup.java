package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.mainLogic.MinesweeperGUI;

/**
 * Всплывающее окно результата игры.
 * 
 * <p>
 * Отображается поверх игрового поля по окончании партии —
 * при победе ({@link #showWin}) или поражении ({@link #showLose}).
 * Окно модальное, блокирует взаимодействие с родительским окном
 * до закрытия и центрируется относительно него.
 * </p>
 * 
 * <p>
 * Пример использования:
 * </p>
 * 
 * <pre>{@code
 * GameOverPopup.showWin(primaryStage, 120, gui, 16, 30, 99);
 * GameOverPopup.showLose(primaryStage, 45, gui, 9, 9, 10);
 * }</pre>
 * 
 * @author Maxim
 * @version 1.0
 * @since 1.0
 * @see StatsPopup
 */
public class GameOverPopup {

        /**
         * Запрещает создание экземпляров утилитного класса.
         */
        private GameOverPopup() {
        }

        /**
         * Показывает окно поражения.
         * 
         * <p>
         * Отображает заголовок «ВЗРЫВ», время партии и кнопки
         * для повторной игры или возврата в главное меню.
         * </p>
         * 
         * @param owner   родительское окно игры
         * @param seconds время партии в секундах
         * @param gui     экземпляр главного приложения для перезапуска или выхода в
         *                меню
         * @param rows    количество строк игрового поля
         * @param cols    количество столбцов игрового поля
         * @param mines   количество мин на поле
         * @see #showWin
         */
        public static void showLose(Stage owner, int seconds, MinesweeperGUI gui, int rows,
                        int cols, int mines) {
                show(owner, seconds, gui, rows, cols, mines, false);
        }

        /**
         * Показывает окно победы.
         * 
         * <p>
         * Отображает заголовок «ПОБЕДА!», время партии и кнопки
         * для повторной игры или возврата в главное меню.
         * </p>
         * 
         * @param owner   родительское окно игры
         * @param seconds время партии в секундах
         * @param gui     экземпляр главного приложения для перезапуска или выхода в
         *                меню
         * @param rows    количество строк игрового поля
         * @param cols    количество столбцов игрового поля
         * @param mines   количество мин на поле
         * @see #showLose
         */
        public static void showWin(Stage owner, int seconds, MinesweeperGUI gui, int rows, int cols,
                        int mines) {
                show(owner, seconds, gui, rows, cols, mines, true);
        }

        /**
         * Внутренний метод построения и отображения попапа.
         * 
         * <p>
         * Создаёт {@link Stage} с прозрачным фоном, строит макет
         * из заголовка, подзаголовка, времени и кнопок управления,
         * затем центрирует окно относительно {@code owner}.
         * </p>
         * 
         * @param owner   родительское окно
         * @param seconds время партии в секундах
         * @param gui     экземпляр главного приложения
         * @param rows    строки поля
         * @param cols    столбцы поля
         * @param mines   количество мин
         * @param win     {@code true} — победа, {@code false} — поражение
         */
        private static void show(Stage owner, int seconds, MinesweeperGUI gui, int rows, int cols,
                        int mines, boolean win) {
                Stage popupStage = new Stage(StageStyle.TRANSPARENT);
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.initOwner(owner);

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

                StackPane root = new StackPane(box);
                root.setStyle("-fx-background-color: transparent;");
                root.setAlignment(Pos.CENTER);

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().add(
                                GameOverPopup.class.getResource("/gui/style.css").toExternalForm());

                popupStage.setScene(scene);
                popupStage.sizeToScene();

                popupStage.setOnShown(e -> {
                        popupStage.setX(owner.getX() + (owner.getWidth() - popupStage.getWidth()) / 2);
                        popupStage.setY(owner.getY() + (owner.getHeight() - popupStage.getHeight()) / 2);
                });

                popupStage.show();
        }

        /**
         * Создаёт стилизованную кнопку для попапа.
         * 
         * <p>
         * Кнопка имеет прозрачный фон, цветную рамку и текст.
         * При наведении снижает прозрачность до 75%.
         * </p>
         * 
         * @param text  текст кнопки
         * @param color hex-цвет рамки и текста, например {@code "#4ade80"}
         * @return готовая кнопка с hover-эффектом
         */
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