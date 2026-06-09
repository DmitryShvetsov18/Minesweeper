package logic.mainLogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Графическое представление клетки игрового поля сапёра.
 * <p>
 * Класс связывает логическую модель {@link Cell} с её отображением
 * в пользовательском интерфейсе JavaFX.
 * В зависимости от состояния клетки отображает соответствующее изображение:
 * мину, флаг, число соседних мин или пустую клетку.
 * </p>
 */
public class CellPane extends StackPane {

    private final int row;
    private final int column;
    private final Cell cell;

    private final ImageView imageView;

    /**
     * Создает графическое представление клетки.
     *
     * @param row номер строки клетки
     * @param column номер столбца клетки
     * @param cell логическая модель клетки
     */
    public CellPane(int row, int column, Cell cell) {
        this.row = row;
        this.column = column;
        this.cell = cell;

        setPrefSize(50, 50);
        setMaxSize(50, 50);
        setMinSize(50, 50);

        imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        imageView.setPreserveRatio(true);

        getChildren().add(imageView);
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

    /**
     * Обновляет внешний вид клетки в соответствии с её текущим состоянием.
     * <p>
     * Метод анализирует состояние связанной модели {@link Cell}
     * и устанавливает необходимые CSS-классы и изображения:
     * </p>
     * <ul>
     *     <li>закрытая клетка.</li>
     *     <li>открытая пустая клетка;</li>
     *     <li>открытая клетка с числом соседних мин;</li>
     *     <li>клетка с установленным флагом;</li>
     *     <li>открытая клетка с миной;</li>
     * </ul>
     */
    public void updateDisplay() {
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
                    getStyleClass().add("cell-empty");
                    imageView.setImage(null);
                }
            }
        } else if (cell.IsFlagged()) {
            getStyleClass().add("cell-flag");
            imageView.setImage(load("/images/flag.png"));
        } else {
            imageView.setImage(null);
        }
    }

    /**
     * Загружает изображение из ресурсов приложения.
     *
     * @param path путь к изображению относительно каталога ресурсов
     * @return загруженное изображение или {@code null},
     *         если файл не найден
     */
    private Image load(String path) {
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Image not found: " + path);
            return null;
        }
    }
}
