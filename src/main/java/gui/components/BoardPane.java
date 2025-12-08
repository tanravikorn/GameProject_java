package gui.components;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import logic.board.Board;
import logic.utils.Point;

import java.util.Set;
import java.util.function.BiConsumer;

public class BoardPane extends GridPane {

    public BoardPane() {
        this.setAlignment(Pos.CENTER);
    }

    public void update(Board board, Set<Point> animateCandidates,
                       int selectedRow, int selectedCol,
                       BiConsumer<Integer, Integer> onTileClick) {

        this.getChildren().clear();

        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                boolean shouldHide = (animateCandidates != null &&
                        animateCandidates.contains(new Point(r, c)));
                boolean isSelected = (r == selectedRow && c == selectedCol);

                // ส่ง onTileClick เข้าไปใน Constructor เลย
                CandyTile tile = new CandyTile(r, c, board.getCandy(r, c), isSelected, shouldHide, onTileClick);

                this.add(tile, c, r);
            }
        }
    }
}