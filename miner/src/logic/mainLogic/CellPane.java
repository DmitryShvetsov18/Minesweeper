package logic.mainLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CellPane extends StackPane {

    private final int row;
    private final int column;
    private final Cell cell;

    private final ImageView imageView;

    public CellPane(int row, int column, Cell cell) {
        this.row = row;
        this.column = column;
        this.cell = cell;

        setPrefSize(50, 50);
        setMaxSize(50, 50);
        setMinSize(50, 50);

        // Убрали Rectangle, будем использовать фон самого StackPane через CSS

        imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        imageView.setPreserveRatio(true);

        // Центрируем картинку
        getChildren().add(imageView);

        // Базовый стиль для всех клеток
        getStyleClass().add("cell-btn");

        updateDisplay();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell getCell() {
        return cell;
    }

    public void updateDisplay() {
        // Сбрасываем специфические состояния
        getStyleClass().removeAll("cell-open", "cell-flag", "cell-mine", "cell-empty");

        if (cell.IsOpen()) {
            if (cell.IsMine()) {
                getStyleClass().add("cell-mine");
                imageView.setImage(load("/images/mine.png"));
            } else {
                getStyleClass().add("cell-open");

                int n = cell.getCountOfNeighbourMines();
                if (n > 0) {
                    imageView.setImage(load("/images/" + n + ".png"));
                } else {
                    // Пустая открытая клетка
                    getStyleClass().add("cell-empty");
                    imageView.setImage(null);
                }
            }
        } else if (cell.IsFlagged()) {
            getStyleClass().add("cell-flag");
            imageView.setImage(load("/images/flag.png"));
        } else {
            // Закрытая клетка (стандартное состояние)
            // Убеждаемся, что картинки нет
            imageView.setImage(null);
        }
    }

    private Image load(String path) {
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Image not found: " + path);
            return null;
        }
    }
}
